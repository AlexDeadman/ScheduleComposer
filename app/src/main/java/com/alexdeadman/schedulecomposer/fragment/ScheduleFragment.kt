package com.alexdeadman.schedulecomposer.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.alexdeadman.schedulecomposer.R
import com.alexdeadman.schedulecomposer.adapter.ScheduleItem
import com.alexdeadman.schedulecomposer.databinding.FragmentScheduleBinding
import com.alexdeadman.schedulecomposer.model.entity.*
import com.alexdeadman.schedulecomposer.util.launchRepeatingCollect
import com.alexdeadman.schedulecomposer.util.provideViewModel
import com.alexdeadman.schedulecomposer.util.state.ListState.*
import com.alexdeadman.schedulecomposer.viewmodel.*
import com.bin.david.form.data.column.Column
import com.bin.david.form.data.table.TableData
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
            smartTable.apply {
                config.apply {
                    isShowTableTitle = false
                    isShowXSequence = false
                    isShowYSequence = false
                }
                setOnColumnClickListener {
                    val list = it.column.datas
                        .distinct()
                        .map { any -> any.toString() }
                        .sorted()

                    AlertDialog.Builder(context) // FIXME:  
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
                setZoom(true, 1.5f, 0.5f)
            }

            val viewModels = listOf(
                ScheduleViewModel::class,
                GroupsViewModel::class,
                DisciplinesViewModel::class,
                ClassroomsViewModel::class,
                LecturersViewModel::class
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
                            val groups = results[1]
                                .map { it as Group }
                                .filter {
                                    val syllabusId = requireArguments().getInt("syllabus_id")
                                    it.relationships!!.syllabus.data.id == syllabusId
                                }

                            val scheduleItems = results[0]
                                .asSequence()
                                .map { it as Schedule }
                                .filter { it.relationships!!.group.data.id in groups.map { g -> g.id } }
                                .filter { it.attributes.semester == requireArguments().getInt("semester") }
                                .map { schedule ->
                                    ScheduleItem(
                                        requireContext(),
                                        schedule,
                                        groups,
                                        results[2].map { it as Discipline },
                                        results[3].map { it as Classroom },
                                        results[4].map { it as Lecturer },
                                    )
                                }
                                .sortedWith(
                                    compareBy(
                                        { it.group },
                                        { it.evenWeek },
                                        { it.weekDay },
                                        { it.period }
                                    )
                                )
                                .toList()

                            val tableData = TableData(
                                "",
                                scheduleItems,
                                Column<String>(getString(R.string.column_group), "group"),
                                Column<String>(getString(R.string.column_week), "weekName"),
                                Column<String>(getString(R.string.column_day), "dayName"),
                                Column<Int>(getString(R.string.column_period), "period"),
                                Column<Int>(getString(R.string.column_discipline), "discipline"),
                                Column<Int>(getString(R.string.column_classroom), "classroom"),
                                Column<Int>(getString(R.string.column_lecturer), "lecturer"),
                                Column<Int>(getString(R.string.column_type), "type"),
                            )

                            smartTable.apply {
//                                setData(scheduleItems)
                                setTableData(tableData)
//                                tableData.columns
//                                    .zip(ScheduleItem.columnNamesIds)
//                                    .forEach { it.first.columnName = getString(it.second) }
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
                        //
                        /* tempo */ isEnabled = false /* tempo */
                        //
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