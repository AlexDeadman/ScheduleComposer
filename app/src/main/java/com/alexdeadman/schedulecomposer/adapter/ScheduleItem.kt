package com.alexdeadman.schedulecomposer.adapter

import android.content.Context
import android.graphics.Paint
import com.alexdeadman.schedulecomposer.R
import com.alexdeadman.schedulecomposer.model.entity.*
import com.bin.david.form.annotation.SmartColumn
import com.bin.david.form.annotation.SmartTable

@SmartTable
class ScheduleItem(
    context: Context,
    schedule: Schedule,
    lecturers: List<Lecturer>,
    disciplines: List<Discipline>,
    groups: List<Group>,
    classrooms: List<Classroom>,
) {
    private val attributes: Schedule.ScheduleAttributes = schedule.attributes
    private val relationships: Schedule.ScheduleRelationships = schedule.relationships!!

    val weekDay = attributes.weekDay
    val evenWeek = attributes.evenWeek

    @SmartColumn(id = 1, autoMerge = true)
    val semester: Int = attributes.semester

    @SmartColumn(id = 2, autoMerge = true)
    val group: String = groups.single {
        it.id == relationships.group.data.id
    }.title

    @SmartColumn(id = 3, autoMerge = true)
    val weekName: String = context.getString(if (evenWeek) R.string.even else R.string.odd)

    @SmartColumn(id = 4, autoMerge = true)
    val dayName: String = context.getString(
        when (weekDay) {
            1 -> R.string.monday
            2 -> R.string.tuesday
            3 -> R.string.wednesday
            4 -> R.string.thursday
            5 -> R.string.friday
            6 -> R.string.saturday
            else -> throw IllegalStateException()
        }
    )

    @SmartColumn(id = 5)
    val period = attributes.period

    @SmartColumn(id = 6, align = Paint.Align.LEFT)
    val discipline: String = disciplines.single {
        it.id == relationships.discipline.data.id
    }.title

    @SmartColumn(id = 7)
    val classroom: String = classrooms.single {
        it.id == relationships.classroom.data.id
    }.title

    @SmartColumn(id = 8)
    val lecturer: String = lecturers.single {
        it.id == relationships.lecturer.data.id
    }.shortTitle

    @SmartColumn(id = 9)
    val typeName: String = context.getString(
        when (attributes.type) {
            1 -> R.string.lec
            2 -> R.string.lab
            3 -> R.string.prac
            4 -> R.string.isw
            else -> R.string.unknown_type
        }
    )

}