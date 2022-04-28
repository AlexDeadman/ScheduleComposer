package com.alexdeadman.schedulecomposer.adapter

import android.content.Context
import com.alexdeadman.schedulecomposer.R
import com.alexdeadman.schedulecomposer.model.entity.*

//@SmartTable
class ScheduleItem(
    context: Context,
    schedule: Schedule,
    groups: List<Group>,
    disciplines: List<Discipline>,
    classrooms: List<Classroom>,
    lecturers: List<Lecturer>,
) {
//    companion object {
//        val columnNamesIds = listOf(
//            R.string.column_group,
//            R.string.column_week,
//            R.string.column_day,
//            R.string.column_period,
//            R.string.column_discipline,
//            R.string.column_classroom,
//            R.string.column_lecturer,
//            R.string.column_type,
//        )
//    }

    private val attributes: Schedule.ScheduleAttributes = schedule.attributes
    private val relationships: Schedule.ScheduleRelationships = schedule.relationships!!

    val weekDay = attributes.weekDay
    val evenWeek = attributes.evenWeek

    //    @SmartColumn(id = 1, autoMerge = true)
    val group: String = groups.singleOrNull {
        it.id == relationships.group.data.id
    }?.title ?: context.getString(R.string.unknown)

    //    @SmartColumn(id = 2, autoMerge = true)
    val weekName: String = context.getString(schedule.weekNameId)

    //    @SmartColumn(id = 3, autoMerge = true)
    val dayName: String = context.getString(schedule.dayNameId)

    //    @SmartColumn(id = 4)
    val period = attributes.period

    private val disciplineTitle = disciplines.singleOrNull {
        it.id == relationships.discipline.data.id
    }?.title ?: context.getString(R.string.unknown)

//    @SmartColumn(id = 5, align = Paint.Align.LEFT)
    val discipline: String = // FIXME:
        if (disciplineTitle.length > 30) disciplineTitle.take(27) + "..." else disciplineTitle

//    @SmartColumn(id = 6)
    val classroom = classrooms.singleOrNull {
        it.id == relationships.classroom.data.id
    }?.title ?: context.getString(R.string.unknown)

//
//    @SmartColumn(id = 7, align = Paint.Align.LEFT)
    val lecturer: String = lecturers.singleOrNull {
        it.id == relationships.lecturer.data.id
    }?.shortTitle ?: context.getString(R.string.unknown)
//
//    @SmartColumn(id = 8)
    val type: String = context.getString(schedule.typeName)

}