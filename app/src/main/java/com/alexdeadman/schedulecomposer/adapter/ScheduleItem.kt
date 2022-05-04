package com.alexdeadman.schedulecomposer.adapter

import android.content.Context
import com.alexdeadman.schedulecomposer.model.entity.Schedule

class ScheduleItem(
    context: Context,
    schedule: Schedule,
    val group: String?,
    val discipline: String?,
    val lecturer: String?,
    val classroom: String?,
) {
    private val attributes = schedule.attributes
    private val relationships = schedule.relationships!!

    val type = context.getString(Schedule.typeIds[attributes.type - 1])
    val week = context.getString(Schedule.weekNameIds[if(attributes.evenWeek) 0 else 1])
    val day = context.getString(Schedule.dayNameIds[attributes.weekDay - 1])
    val period = attributes.period
}