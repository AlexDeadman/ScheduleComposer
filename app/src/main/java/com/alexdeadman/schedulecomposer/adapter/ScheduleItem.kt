package com.alexdeadman.schedulecomposer.adapter

import android.content.Context
import com.alexdeadman.schedulecomposer.model.entity.Schedule
import com.alexdeadman.schedulecomposer.model.entity.Schedule.Companion.dayNameIds
import com.alexdeadman.schedulecomposer.model.entity.Schedule.Companion.typeIds
import com.alexdeadman.schedulecomposer.model.entity.Schedule.Companion.weekNameIds
import com.alexdeadman.schedulecomposer.util.ellipsize

class ScheduleItem(
    context: Context,
    schedule: Schedule,
    private val group: String?,
    private val discipline: String?,
    private val lecturer: String?,
    val classroom: String?,
) {
    private val attributes = schedule.attributes

    val type = context.getString(typeIds[attributes.type - 1])
    val week = context.getString(weekNameIds[if (attributes.evenWeek) 0 else 1])
    val day = context.getString(dayNameIds[attributes.weekDay - 1])
    val period = attributes.period

    val title get() = "$group $discipline $lecturer $type"

    override fun toString(): String = "$group $discipline".ellipsize(20)
}