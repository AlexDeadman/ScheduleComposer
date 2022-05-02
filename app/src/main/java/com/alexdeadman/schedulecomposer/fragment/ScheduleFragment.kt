package com.alexdeadman.schedulecomposer.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.alexdeadman.schedulecomposer.R
import com.alexdeadman.schedulecomposer.databinding.FragmentScheduleBinding
import com.alexdeadman.schedulecomposer.model.entity.*
import com.alexdeadman.schedulecomposer.model.entity.Schedule.Companion.columnNameIds
import com.alexdeadman.schedulecomposer.model.entity.Schedule.Companion.dayNameIds
import com.alexdeadman.schedulecomposer.model.entity.Schedule.Companion.typeIds
import com.alexdeadman.schedulecomposer.model.entity.Schedule.Companion.weekNameIds
import com.alexdeadman.schedulecomposer.util.launchRepeatingCollect
import com.alexdeadman.schedulecomposer.util.provideViewModel
import com.alexdeadman.schedulecomposer.util.state.ListState.*
import com.alexdeadman.schedulecomposer.viewmodel.*
import com.bin.david.form.data.format.draw.TextDrawFormat
import com.bin.david.form.data.table.ArrayTableData
import com.google.android.material.color.MaterialColors
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import javax.inject.Inject


@AndroidEntryPoint
class ScheduleFragment : Fragment() {

    private var _binding: FragmentScheduleBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentScheduleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            val columnNames = columnNameIds.map { getString(it) }

            smartTable.apply {
                config.apply {
                    isShowXSequence = false
                    isShowYSequence = false
                }
                setOnColumnClickListener {

                    if (it.column.columnName !in columnNames) return@setOnColumnClickListener

                    val list = it.column.datas
                        .distinct()
                        .map { any -> any.toString() }

                    // FIXME: memory leak etc
                    AlertDialog.Builder(context)
                        .setMultiChoiceItems(
                            list.toTypedArray(),
                            list.map { true }.toBooleanArray()
                        ) { _, _, _ -> }
                        .setTitle(it.column.columnName)
                        .setNegativeButton(R.string.all, null)
                        .setNeutralButton(R.string.clear, null)
                        .setPositiveButton(R.string.apply, null)
                        .create()
                        .show()
                }
                setZoom(true, 1f, 0.33f)
            }

            val viewModels = listOf(
                ScheduleViewModel::class,
                GroupsViewModel::class,
                DisciplinesViewModel::class,
                ClassroomsViewModel::class,
                LecturersViewModel::class,
                SyllabusesViewModel::class
            ).map {
                provideViewModel(viewModelFactory, it)
            }

            combine(
                *viewModels.map { it.state.filterNotNull() }.toTypedArray()
            ) { stateArray -> stateArray.toList() }
                .launchRepeatingCollect(viewLifecycleOwner) { stateList ->
                    when (val scheduleState = stateList.first()) {
                        is Loaded -> {
                            if (stateList.any { it !is Loaded }) {
                                viewModels.first().state.value = Error(R.string.unknown_error)
                                return@launchRepeatingCollect
                            }

                            textViewMassage.visibility = View.GONE

                            val results = stateList.map { (it as Loaded).result.data }

                            val syllabusId = requireArguments().getInt("syllabus_id")
                            val groups = results[1]
                                .map { it as Group }
                                .filter { it.relationships!!.syllabus.data.id == syllabusId }

                            val semester = requireArguments().getInt("semester")
                            val groupIds = groups.map { it.id }
                            val schedules = results[0]
                                .map { it as Schedule }
                                .filter { it.attributes.semester == semester }
                                .filter { it.relationships!!.group.data.id in groupIds }

                            val disciplines = results[2].map { it as Discipline }

                            val classrooms = results[3]
                                .map { it as Classroom }
                                .sortedBy { it.title }

                            val lecturers = results[4].map { it as Lecturer }

                            val syllabus = results[5]
                                .map { it as Syllabus }
                                .single { it.id == syllabusId }

                            val titleNames = columnNames.plus(classrooms.map { it.title })

                            smartTable.tableData = ArrayTableData.create(
                                "${syllabus.attributes.name.take(30).plus("...")} > " +
                                        "${syllabus.attributes.year} > " +
                                        "$semester",
                                titleNames.toTypedArray(),
                                Array(titleNames.size) { i ->
                                    var day = -1
                                    Array(2 * 6 * 8) { j ->
                                        val week = j / 6 / 8
                                        val period = j % 8 + 1
                                        if (period == 1) day++
                                        when (i) {
                                            0 -> getString(weekNameIds[week])
                                            1 -> getString(dayNameIds[day - week * 6])
                                            2 -> period
                                            else -> {
                                                schedules.find { sch ->
                                                    sch.attributes.let { attr ->
                                                        listOf(
                                                            attr.evenWeek == (week == 0),
                                                            attr.weekDay == day - week * 6 + 1,
                                                            attr.period == period,
                                                            sch.relationships!!.classroom.data.id ==
                                                                    classrooms[i - 3].id
                                                        ).all { it }
                                                    }
                                                }?.let { sch ->
                                                    val group = groups.find {
                                                        it.id == sch.relationships!!.group.data.id
                                                    }?.title
                                                    val discipline = disciplines.find {
                                                        it.id == sch.relationships!!.discipline.data.id
                                                    }?.title
                                                    val lecturer = lecturers.find {
                                                        it.id == sch.relationships!!.lecturer.data.id
                                                    }?.shortTitle
                                                    val type = getString(
                                                        typeIds[sch.attributes.type - 1]
                                                    )
                                                    "$group $discipline $lecturer $type"
                                                        .take(20)
                                                        .plus("...")
                                                }
                                            }
                                        }
                                    }
                                },
                                TextDrawFormat()
                            )

                            smartTable.tableData.columns.run {
                                take(2).forEach { it.isAutoMerge = true } // FIXME 
                                take(3).forEach { it.isFixed = true }
                            }
                        }
                        is NoItems -> {
                            textViewMassage.apply {
                                visibility = View.VISIBLE
                                text = getString(R.string.table_empty)
                            }
                        }
                        is Error -> {
                            textViewMassage.apply {
                                visibility = View.VISIBLE
                                text = getString(scheduleState.messageStringId)
                            }
                        }
                    }
                    progressBar.visibility = View.GONE
                    swipeRefreshLayout.apply {
                        /* tempo */ isEnabled = false /* tempo */
                        visibility = View.VISIBLE
                        isRefreshing = false
                    }
                }

            swipeRefreshLayout.apply {
                setOnRefreshListener {
                    viewModels.first().getEntities()
                }
                setColorSchemeResources(android.R.color.white)
                setProgressBackgroundColorSchemeColor(
                    MaterialColors.getColor(view, R.attr.colorPrimary)
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}