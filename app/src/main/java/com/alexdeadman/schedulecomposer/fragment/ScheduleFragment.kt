package com.alexdeadman.schedulecomposer.fragment

import android.graphics.Paint
import android.os.Bundle
import android.view.*
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
import com.bin.david.form.data.CellRange
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

    private lateinit var viewModels: List<AbstractEntityViewModel>

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

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
                setZoom(true, 1f, 0.3f)
            }

            viewModels = listOf(
                ScheduleViewModel::class,
                GroupsViewModel::class,
                DisciplinesViewModel::class,
                ClassroomsViewModel::class,
                LecturersViewModel::class,
            ).map {
                provideViewModel(viewModelFactory, it)
            }

            combine(viewModels.map { it.state.filterNotNull() }) { stateArray ->
                stateArray.toList()
            }.launchRepeatingCollect(viewLifecycleOwner) { stateList ->
                when (val scheduleState = stateList.first()) {
                    is Loaded, is NoItems -> {
                        if (stateList.any { it is Error }) {
                            viewModels.first().state.value = Error(R.string.unknown_error)
                            return@launchRepeatingCollect
                        }

                        val results = stateList.map {
                            if (it is Loaded) it.result.data
                            else emptyList()
                        }

                        val syllabus = requireArguments()
                            .getParcelable<Syllabus>(BundleKeys.SYLLABUS)!!
                        val groups = results[1]
                            .map { it as Group }
                            .filter { it.relationships!!.syllabus.data.id == syllabus.id }

                        val semester = requireArguments().getInt(BundleKeys.SEMESTER)
                        val groupIds = groups.map { it.id }
                        val schedules = results[0]
                            .map { it as Schedule }
                            .filter {
                                it.attributes.semester == semester &&
                                    it.relationships!!.group.data.id in groupIds
                            }

                        val disciplines = results[2]
                            .map { it as Discipline }
                            .filter { it.relationships!!.syllabus.data.id == syllabus.id }
                        val classrooms = results[3].map { it as Classroom }
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

                        val weekTypeCount = 2
                        val dayCount = 6
                        val periodCount = 8
                        val rowCount = weekTypeCount * dayCount * periodCount

                        val weekColumnData = Array(rowCount) {
                            getString(weekNameIds[it / dayCount / periodCount])
                        }

                        val periodColumnData = Array(rowCount) { it % periodCount + 1 }

                        var dayInd = -1
                        val dayColumnData = Array(rowCount) {
                            if (periodColumnData[it] == 1) dayInd++
                            val half = it / dayCount / periodCount // 0 or 1
                            getString(dayNameIds[dayInd - half * dayCount])
                        }

                        val classroomTitles = classrooms.map { it.title }.sorted()
                        val classroomColumnsData = Array(classroomTitles.size) { columnInd ->
                            Array(rowCount) { rowInd ->
                                scheduleItems.find { sch ->
                                    listOf(
                                        sch.week == weekColumnData[rowInd],
                                        sch.day == dayColumnData[rowInd],
                                        sch.period == periodColumnData[rowInd],
                                        sch.classroom == classroomTitles[columnInd]
                                    ).all { it }
                                }
                            }
                        }

                        val tableName = syllabus.attributes.let {
                            "${it.name.ellipsize(30)} > ${it.year} > $semester"
                        }
                        columnNames.addAll(classroomTitles)
                        val tableDataData = arrayOf(
                            weekColumnData,
                            dayColumnData,
                            periodColumnData,
                            *classroomColumnsData
                        )

                        smartTable.apply {
                            tableData = ArrayTableData.create(
                                tableName,
                                columnNames.toTypedArray(),
                                tableDataData,
                                TextDrawFormat()
                            ).apply {
                                val columnsRanges = columns
                                    .take(2)
                                    .onEach { it.isAutoMerge = true }
                                    .map { column -> column.parseRanges().map { it as IntArray } }

                                userCellRange = columnsRanges.map { column ->
                                    column.map { range ->
                                        val columnIndex = columnsRanges.indexOf(column)
                                        CellRange(range[0], range[1], columnIndex, columnIndex)
                                    }
                                }.flatten()
                            }

                            tableData.columns.run {
                                take(3).forEach { it.isFixed = true }
                                drop(3).forEach {
                                    it.setOnColumnItemClickListener { column, _, _, position ->
                                        val item = column.datas.getOrNull(position) as ScheduleItem?
                                        textViewScheduleItem.text = item?.title
                                    }
                                }
                            }

                            setSelectFormat { canvas, rect, _, config ->
                                val paint = config.paint.apply {
                                    color = MaterialColors.getColor(view, R.attr.colorPrimary)
                                    style = Paint.Style.STROKE
                                    strokeWidth = 6f
                                }
                                canvas.drawRect(rect, paint)
                            }

                            visibility = View.VISIBLE
                        }

                        textViewMassage.visibility = View.GONE
                    }
                    is Error -> {
                        smartTable.visibility = View.GONE
                        textViewMassage.apply {
                            visibility = View.VISIBLE
                            text = getString(scheduleState.messageStringId)
                        }
                    }
                }
                progressBar.visibility = View.GONE
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_refresh, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_refresh) {
            binding.progressBar.visibility = View.VISIBLE
            viewModels.first().getEntities()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}