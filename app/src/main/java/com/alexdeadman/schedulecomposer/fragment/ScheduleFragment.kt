package com.alexdeadman.schedulecomposer.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.alexdeadman.schedulecomposer.R
import com.alexdeadman.schedulecomposer.adapter.ScheduleItem
import com.alexdeadman.schedulecomposer.databinding.FragmentScheduleBinding
import com.alexdeadman.schedulecomposer.model.entity.*
import com.alexdeadman.schedulecomposer.model.entity.Schedule.Companion.columnNameIds
import com.alexdeadman.schedulecomposer.model.entity.Schedule.Companion.dayNameIds
import com.alexdeadman.schedulecomposer.model.entity.Schedule.Companion.weekNameIds
import com.alexdeadman.schedulecomposer.util.ellipsize
import com.alexdeadman.schedulecomposer.util.key.BundleKeys
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

            val columnNames = columnNameIds.map { getString(it) }.toMutableList()

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
                setZoom(true, 1f, 0.25f)
            }

            val viewModels = listOf(
                ScheduleViewModel::class,
                GroupsViewModel::class,
                DisciplinesViewModel::class,
                ClassroomsViewModel::class,
                LecturersViewModel::class,
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

                            val syllabus = requireArguments()
                                .getParcelable<Syllabus>(BundleKeys.SYLLABUS)!!
                            val groups = results[1]
                                .map { it as Group }
                                .filter { it.relationships!!.syllabus.data.id == syllabus.id }

                            val semester = requireArguments().getInt(BundleKeys.SEMESTER)
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

                            val scheduleItems = schedules.map { sch ->
                                val rel = sch.relationships!!
                                ScheduleItem(
                                    requireContext(),
                                    sch,
                                    groups.find { it.id == rel.group.data.id }?.title,
                                    disciplines.find { it.id == rel.discipline.data.id }?.title,
                                    lecturers.find { it.id == rel.lecturer.data.id }?.shortTitle,
                                    classrooms.find { it.id == rel.classroom.data.id }?.title
                                )
                            }

                            columnNames.addAll(classrooms.map { it.title })

                            val weekTypeCount = 2
                            val dayCount = 6
                            val periodCount = 8

                            val tableDataData = Array(columnNames.size) { columnInd ->
                                var dayInd = -1
                                Array(weekTypeCount * dayCount * periodCount) { rowInd ->

                                    val weekId = rowInd / dayCount / periodCount
                                    val week = getString(weekNameIds[weekId])

                                    val period = rowInd % periodCount + 1

                                    if (period == 1) dayInd++
                                    val day = getString(dayNameIds[dayInd - weekId * dayCount])

                                    when (columnInd) {
                                        0 -> week
                                        1 -> day
                                        2 -> period
                                        else -> {
                                            scheduleItems.find { sch ->
                                                listOf(
                                                    sch.week == week,
                                                    sch.day == day,
                                                    sch.period == period,
                                                    sch.classroom == columnNames[columnInd]
                                                ).all { it }
                                            }?.let {
                                                "${it.group} ${it.discipline}".ellipsize(20)
                                            }
                                        }
                                    }
                                }
                            }

                            val tableName = syllabus.attributes.let {
                                "${it.name.ellipsize(30)} > ${it.year} > $semester"
                            }

                            smartTable.apply {
                                tableData = ArrayTableData.create(
                                    tableName,
                                    columnNames.toTypedArray(),
                                    tableDataData,
                                    TextDrawFormat()
                                )
                                tableData.columns
                                    .take(3)
                                    .onEach { it.isFixed = true }
                                    .dropLast(1)
                                    .forEach { it.isAutoMerge = true } // FIXME
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