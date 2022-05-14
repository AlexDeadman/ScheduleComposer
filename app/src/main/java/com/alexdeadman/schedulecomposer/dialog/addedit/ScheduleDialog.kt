package com.alexdeadman.schedulecomposer.dialog.addedit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import com.alexdeadman.schedulecomposer.R
import com.alexdeadman.schedulecomposer.databinding.FieldsScheduleBinding
import com.alexdeadman.schedulecomposer.model.entity.*
import com.alexdeadman.schedulecomposer.model.entity.Schedule.Companion.dayNameIds
import com.alexdeadman.schedulecomposer.model.entity.Schedule.Companion.typeIds
import com.alexdeadman.schedulecomposer.model.entity.Schedule.Companion.weekNameIds
import com.alexdeadman.schedulecomposer.util.provideViewModel
import com.alexdeadman.schedulecomposer.util.state.ListState
import com.alexdeadman.schedulecomposer.util.validate
import com.alexdeadman.schedulecomposer.viewmodel.*
import com.validator.textinputvalidator.valid
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.KClass

@AndroidEntryPoint
class ScheduleDialog : AbstractAddEditDialog<FieldsScheduleBinding>() {

    override val entityTitleId = R.string.schedule
    override val mainViewModelClass = ScheduleViewModel::class
    override val relatedViewModelClass: KClass<out AbstractEntityViewModel>? = null

    override fun createBinding(inflater: LayoutInflater) = FieldsScheduleBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val schedule = currentEntity as Schedule

        val relatives = listOf(
            ClassroomsViewModel::class,
            GroupsViewModel::class,
            DisciplinesViewModel::class,
            LecturersViewModel::class,
        ).map {
            val state = provideViewModel(viewModelFactory, it).listStateFlow.value
            if (state is ListState.Loaded) state.result.data
            else emptyList()
        }

        val classrooms = relatives[0].map { it as Classroom }

        val syllabusId = schedule.relationships!!.syllabus.data.id

        val groups = relatives[1]
            .map { it as Group }
            .filter { it.relationships!!.syllabus.data.id == syllabusId }
            .sortedBy { it.title }

        val disciplines = relatives[2]
            .map { it as Discipline }
            .filter { it.relationships!!.syllabus.data.id == syllabusId }
            .sortedBy { it.title }

        val lecturers = relatives[3]
            .map { it as Lecturer }
            .sortedBy { it.title }

        val types = typeIds.map { getString(it) }

        fieldsBinding.apply {

            schedule.attributes.let { attr ->
                listOf(
                    textViewWeek,
                    textViewDay,
                    textViewPeriod,
                    textViewClassroom
                ).zip(
                    listOf(
                        getString(weekNameIds[if (attr.evenWeek) 0 else 1]),
                        getString(dayNameIds[attr.weekDay - 1]),
                        attr.period.toString(),
                        classrooms.find {
                            it.id == schedule.relationships!!.classroom.data.id
                        }?.title
                    )
                ).map {
                    it.first.text = it.second
                }
            }


            val tiLayouts = listOf(
                tiLayoutGroup,
                tiLayoutDiscipline,
                tiLayoutLecturer,
                tiLayoutType
            )

            freezingViews = tiLayouts

            tiLayouts.forEach { layout ->
                layout.validate(listOf { it.isNotBlank() to getString(R.string.required_field) })
            }

            val acTextViews = listOf(
                acTextViewGroup,
                acTextViewDiscipline,
                acTextViewLecturer,
                acTextViewType
            )

            acTextViews.zip(
                listOf(
                    groups,
                    disciplines,
                    lecturers
                ).map { list ->
                    list.map { it.title }
                }.plusElement(types)
            ).map { pair ->
                pair.first.setAdapter(
                    ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        pair.second
                    )
                )
            }

            if (schedule.id != -1) {
                acTextViews.zip(
                    listOf( // FIXME: crashes if not found
                        groups.indexOfFirst {
                            it.id == schedule.relationships!!.group.data.id
                        },
                        disciplines.indexOfFirst {
                            it.id == schedule.relationships!!.discipline.data.id
                        },
                        lecturers.indexOfFirst {
                            it.id == schedule.relationships!!.lecturer.data.id
                        },
                        schedule.attributes.type - 1
                    )
                ).map {
                    it.first.setText(
                        it.first.adapter.getItem(it.second).toString(),
                        false
                    )
                }
            }

            binding.apply {

                buttonAddEdit.setOnClickListener {
                    if (tiLayouts.all { it.valid() }) {
                        val scheduleCopy = schedule.copy(
                            attributes = schedule.attributes.copy(
                                syllabus = schedule.relationships!!.syllabus.data.id,
                                classroom = schedule.relationships!!.classroom.data.id,
                                group = groups.find {
                                    it.title == acTextViewGroup.text.toString()
                                }?.id,
                                discipline = disciplines.find {
                                    it.title == acTextViewDiscipline.text.toString()
                                }?.id,
                                lecturer = lecturers.find {
                                    it.title == acTextViewLecturer.text.toString()
                                }?.id,
                                type = types.indexOfFirst {
                                    it == acTextViewType.text.toString()
                                } + 1
                            ),
                            relationships = null
                        )

                        if (scheduleCopy.id == -1) mainViewModel.currentEntity = null
                        send(scheduleCopy)
                    }
                }
            }
        }
    }
}