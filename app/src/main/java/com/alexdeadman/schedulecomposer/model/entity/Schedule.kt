package com.alexdeadman.schedulecomposer.model.entity

import com.alexdeadman.schedulecomposer.R
import com.bin.david.form.annotation.ColumnType
import com.bin.david.form.annotation.SmartColumn
import com.bin.david.form.annotation.SmartTable
import com.google.gson.annotations.SerializedName


@SmartTable
data class Schedule(
    override var type: String,
    override var id: Int,
    @SmartColumn(type = ColumnType.Child) override var attributes: ScheduleAttributes,
    @SmartColumn(type = ColumnType.Child) override var relationships: ScheduleRelationships
) : Entity<Schedule.ScheduleAttributes>, Relatable<Schedule.ScheduleRelationships> {

    override val title get() = "Schedule item"
    override val iconId get() = R.drawable.ic_schedule

    override val detailsPhId: Int get() = R.string.ph_schedule_details

    override fun getDetails(relatives: List<Entity<out Attributes>>): List<String> = emptyList()

    data class ScheduleAttributes(
        @SmartColumn var semester: Int,
        @SmartColumn @SerializedName("lecture_type") var lectureType: Int,
        @SmartColumn @SerializedName("week_parity") var weekParity: Boolean,
        @SmartColumn @SerializedName("day_of_the_week") var dayOfTheWeek: Int,
        @SmartColumn @SerializedName("lecture_begin") var lectureBegin: Int,
    ) : Attributes

    data class ScheduleRelationships(
        @SmartColumn(type = ColumnType.Child) var lecturer: Lecturer,
        @SmartColumn(type = ColumnType.Child) var discipline: Discipline,
        @SmartColumn(type = ColumnType.Child) var group: Group,
        @SmartColumn(type = ColumnType.Child) var classroom: Classroom,
    ) : Relationships {
        data class Lecturer(@SmartColumn(type = ColumnType.Child) var data: Data)
        data class Discipline(@SmartColumn(type = ColumnType.Child) var data: Data)
        data class Group(@SmartColumn(type = ColumnType.Child) var data: Data)
        data class Classroom(@SmartColumn(type = ColumnType.Child) var data: Data)
        data class Data(@SmartColumn var id: Int)
    }
}
