package com.alexdeadman.schedulecomposer.fragment

import android.graphics.Paint
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.alexdeadman.schedulecomposer.R
import com.alexdeadman.schedulecomposer.adapter.ScheduleItem
import com.alexdeadman.schedulecomposer.databinding.FragmentScheduleBinding
import com.alexdeadman.schedulecomposer.dialog.ConfirmationDialog
import com.alexdeadman.schedulecomposer.dialog.addedit.ScheduleDialog
import com.alexdeadman.schedulecomposer.model.entity.*
import com.alexdeadman.schedulecomposer.model.entity.Schedule.Companion.columnNameIds
import com.alexdeadman.schedulecomposer.model.entity.Schedule.Companion.dayNameIdsShort
import com.alexdeadman.schedulecomposer.model.entity.Schedule.Companion.typeIds
import com.alexdeadman.schedulecomposer.model.entity.Schedule.Companion.weekNameIdsShort
import com.alexdeadman.schedulecomposer.util.ellipsize
import com.alexdeadman.schedulecomposer.util.key.BundleKeys
import com.alexdeadman.schedulecomposer.util.launchRepeatingCollect
import com.alexdeadman.schedulecomposer.util.provideViewModel
import com.alexdeadman.schedulecomposer.util.show
import com.alexdeadman.schedulecomposer.util.state.ListState
import com.alexdeadman.schedulecomposer.util.state.SendingState
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
class ScheduleFragment : Fragment(), ConfirmationDialog.ConfirmationListener {

    private var _binding: FragmentScheduleBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModels: List<AbstractEntityViewModel>
    private val mainViewModel get() = viewModels.first()

    override var confirmationMessage: String? = null
    private val confirmationDialog = ConfirmationDialog()

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
                setZoom(true, 1f, 0.25f)
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

            combine(viewModels.map { it.listStateFlow.filterNotNull() }) { stateArray ->
                stateArray.toList()
            }.launchRepeatingCollect(viewLifecycleOwner) { stateList ->
                when (val scheduleState = stateList.first()) {
                    is ListState.Loaded, is ListState.NoItems -> {
                        if (stateList.any { it is ListState.Error }) {
                            mainViewModel.listStateFlow
                                .value = ListState.Error(R.string.unknown_error)
                            return@launchRepeatingCollect
                        }

                        val results = stateList.map {
                            if (it is ListState.Loaded) it.result.data
                            else emptyList()
                        }

                        val semester = requireArguments().getInt(BundleKeys.SEMESTER)
                        val syllabus = requireArguments()
                            .getParcelable<Syllabus>(BundleKeys.SYLLABUS)!!


                        val schedules = results[0]
                            .map { it as Schedule }
                            .filter {
                                it.attributes.semester == semester &&
                                    it.relationships!!.syllabus.data.id == syllabus.id
                            }

                        val groups = results[1]
                            .map { it as Group }
                            .filter { it.relationships!!.syllabus.data.id == syllabus.id }

                        val disciplines = results[2]
                            .map { it as Discipline }
                            .filter { it.relationships!!.syllabus.data.id == syllabus.id }

                        val classrooms = results[3].map { it as Classroom }
                        val lecturers = results[4].map { it as Lecturer }

                        val types = typeIds.map { getString(it) }
                        val weekNames = weekNameIdsShort.map { getString(it) }
                        val dayNames = dayNameIdsShort.map { getString(it) }

                        val scheduleItems = schedules.map { schedule ->
                            val rel = schedule.relationships!!
                            val attr = schedule.attributes
                            ScheduleItem(
                                schedule,
                                types[attr.type - 1],
                                weekNames[if (attr.evenWeek) 0 else 1],
                                dayNames[attr.weekDay - 1],
                                attr.period,
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
                            weekNames[it / dayCount / periodCount]
                        }

                        val periodColumnData = Array(rowCount) { it % periodCount + 1 }

                        var dayInd = -1
                        val dayColumnData = Array(rowCount) {
                            if (periodColumnData[it] == 1) dayInd++
                            val half = it / dayCount / periodCount
                            dayNames[dayInd - half * dayCount]
                        }

                        val classroomTitles = classrooms.map { it.title }.sorted()
                        val classroomColumnsData = Array(classroomTitles.size) { columnInd ->
                            Array(rowCount) { rowInd ->
                                scheduleItems.find { item ->
                                    listOf(
                                        item.week == weekColumnData[rowInd],
                                        item.day == dayColumnData[rowInd],
                                        item.period == periodColumnData[rowInd],
                                        item.classroom == classroomTitles[columnInd]
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

                                userCellRange = columnsRanges.mapIndexed { index, column ->
                                    column.map { range ->
                                        CellRange(range[0], range[1], index, index)
                                    }
                                }.flatten()
                            }

                            tableData.columns.run {
                                take(3).forEach { it.isFixed = true }
                                drop(3).forEach { column ->
                                    column.setOnColumnItemClickListener { col, _, _, pos ->

                                        val item = col.datas.getOrNull(pos) as ScheduleItem?
                                        mainViewModel.currentEntity = item?.schedule ?: Schedule(
                                            Schedule.ScheduleAttributes(
                                                semester,
                                                weekNames.indexOf(weekColumnData[pos]) == 0,
                                                dayNames.indexOf(dayColumnData[pos]) + 1,
                                                periodColumnData[pos]
                                            ),
                                            Schedule.ScheduleRelationships(
                                                syllabus.id,
                                                classrooms.find { it.title == col.columnName }!!.id
                                            )
                                        )

                                        if (item != null) {
                                            item.let {
                                                textViewGroup.text = it.group
                                                textViewDiscipline.text = it.discipline
                                                textViewLecturer.text = it.lecturer
                                                textViewType.text = it.type
                                            }
                                            imageButtonAdd.visibility = View.GONE
                                            linearLayoutScheduleItem.visibility = View.VISIBLE
                                        } else {
                                            imageButtonAdd.visibility = View.VISIBLE
                                            linearLayoutScheduleItem.visibility = View.GONE
                                        }
                                    }
                                }
                            }

                            setSelectFormat { canvas, rect, _, config ->
                                canvas.drawRect(
                                    rect,
                                    config.paint.apply {
                                        color = MaterialColors.getColor(view, R.attr.colorPrimary)
                                        style = Paint.Style.STROKE
                                        strokeWidth = 6f
                                    }
                                )
                            }

                            visibility = View.VISIBLE
                        }

                        imageButtonAdd.visibility = View.GONE
                        linearLayoutScheduleItem.visibility = View.GONE

                        textViewMassage.visibility = View.GONE
                    }
                    is ListState.Error -> {
                        smartTable.visibility = View.GONE
                        textViewMassage.apply {
                            visibility = View.VISIBLE
                            text = getString(scheduleState.messageStringId)
                        }
                    }
                }
                progressBar.visibility = View.GONE
            }

            mainViewModel.sendingStateFlow.launchRepeatingCollect(viewLifecycleOwner) {
                if (it is SendingState.Success) {
                    progressBar.visibility = View.VISIBLE
                    mainViewModel.getEntities()
                }
            }

            listOf(imageButtonAdd, imageButtonEdit).forEach {
                it.setOnClickListener { ScheduleDialog().show(childFragmentManager) }
            }
            imageButtonDelete.setOnClickListener { confirmationDialog.show(childFragmentManager) }
        }
    }

    override fun onConfirm() {
        mainViewModel.run { deleteEntity(currentEntity!!.id) }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_refresh, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_refresh) {
            binding.progressBar.visibility = View.VISIBLE
            mainViewModel.getEntities()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}