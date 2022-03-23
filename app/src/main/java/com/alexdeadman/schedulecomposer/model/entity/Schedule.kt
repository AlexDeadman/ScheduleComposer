package com.alexdeadman.schedulecomposer.model.entity

import com.alexdeadman.schedulecomposer.R
import com.google.gson.annotations.SerializedName


data class Schedule(
    override var type: String,
    override var id: Int,
    override var attributes: ScheduleAttributes,
    override var relationships: ScheduleRelationships?
) : Entity<Schedule.ScheduleAttributes, Schedule.ScheduleRelationships> {

    override val title get() = "Schedule item"
    override val iconId get() = R.drawable.ic_schedule

    override val detailsPhId: Int get() = R.string.ph_schedule_details
    override val details get() = mutableListOf("")

    data class ScheduleAttributes(
        @SerializedName("lecture_type")
        var lectureType: Int,

        var semester: Int,

        @SerializedName("week_parity")
        var weekParity: Boolean,

        @SerializedName("day_of_the_week")
        var dayOfTheWeek: Int,

        @SerializedName("lecture_begin")
        var lectureBegin: Int,
    ) : Attributes

    data class ScheduleRelationships(
        var lecturer: Lecturer,
        var discipline: Discipline,
        var group: Group,
        var audience: Audience,
    ) : Relationships {
        data class Data(var id: Int)

        data class Lecturer(var data: Data)
        data class Discipline(var data: Data)
        data class Group(var data: Data)
        data class Audience(var data: Data)
    }
}
