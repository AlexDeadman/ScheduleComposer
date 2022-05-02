package com.alexdeadman.schedulecomposer.model.entity

import com.alexdeadman.schedulecomposer.R
import com.google.gson.annotations.SerializedName


data class Schedule(
    override var type: String,
    override var id: Int,
    override var attributes: ScheduleAttributes,
    override var relationships: ScheduleRelationships? = null,
) : Entity<Schedule.ScheduleAttributes>, Relatable<Schedule.ScheduleRelationships> {

    constructor(id: Int, attributes: ScheduleAttributes) : this("Schedule", id, attributes)

    override val title get() = "Schedule"
    override val iconId get() = R.drawable.ic_schedule

    override val detailsPhId: Int get() = R.string.ph_schedule_details

    override fun getDetails(relatives: List<Entity<out Attributes>>): List<String> = emptyList()

    data class ScheduleAttributes(
        var semester: Int,
        @SerializedName("type") var type: Int,
        @SerializedName("even_week") var evenWeek: Boolean,
        @SerializedName("week_day") var weekDay: Int,
        @SerializedName("period") var period: Int,

        var lecturer: Int? = null,
        var discipline: Int? = null,
        var group: Int? = null,
        var classroom: Int? = null,
    ) : Attributes

    data class ScheduleRelationships(
        var lecturer: Lecturer,
        var discipline: Discipline,
        var group: Group,
        var classroom: Classroom,
    ) : Relationships {
        data class Lecturer(var data: Data)
        data class Discipline(var data: Data)
        data class Group(var data: Data)
        data class Classroom(var data: Data)

        data class Data(var id: Int)
    }

    companion object {
        val columnNameIds = listOf(
            R.string.column_week,
            R.string.column_day,
            R.string.column_period,
        )
        val weekNameIds = listOf(
            R.string.even,
            R.string.odd,
        )
        val dayNameIds = listOf(
            R.string.monday,
            R.string.tuesday,
            R.string.wednesday,
            R.string.thursday,
            R.string.friday,
            R.string.saturday,
        )
        val typeIds = listOf(
            R.string.lec,
            R.string.lab,
            R.string.prac,
            R.string.isw,
        )
    }
}
