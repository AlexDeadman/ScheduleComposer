package com.alexdeadman.schedulecomposer.adapter

import com.alexdeadman.schedulecomposer.model.entity.Schedule
import com.alexdeadman.schedulecomposer.util.ellipsize

class ScheduleItem(
    val schedule: Schedule,
    val type: String,
    val week: String,
    val day: String,
    val period: Int,
    val group: String?,
    val discipline: String?,
    val lecturer: String?,
    val classroom: String?,
) {
    override fun toString(): String = "$group $discipline".ellipsize(20)
}