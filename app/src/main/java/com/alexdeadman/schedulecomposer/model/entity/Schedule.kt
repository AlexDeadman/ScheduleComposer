package com.alexdeadman.schedulecomposer.model.entity

import com.alexdeadman.schedulecomposer.R
import com.google.gson.annotations.SerializedName


data class Schedule(
    override var type: String,
    override var id: Int,
    override var attributes: ScheduleAttributes,
    override var relationships: ScheduleRelationships? = null,
) : Entity<Schedule.ScheduleAttributes>, Relatable<Schedule.ScheduleRelationships> {

    constructor(
        attributes: ScheduleAttributes,
        relationships: ScheduleRelationships?,
    ) : this("Schedule", -1, attributes, relationships)

    override val title get() = "Schedule"
    override val iconId get() = R.drawable.ic_schedule

    override val detailsId: Int? = null

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
        var syllabus: Int? = null,
    ) : Attributes {
        constructor(
            semester: Int,
            evenWeek: Boolean,
            weekDay: Int,
            period: Int,
        ) : this(semester, -1, evenWeek, weekDay, period)
    }

    data class ScheduleRelationships(
        var syllabus: Syllabus,
        var lecturer: Lecturer,
        var discipline: Discipline,
        var group: Group,
        var classroom: Classroom,
    ) : Relationships {

        constructor(syllabusId: Int, classroomId: Int) : this(
            Syllabus(Data(syllabusId)),
            Lecturer(Data(-1)),
            Discipline(Data(-1)),
            Group(Data(-1)),
            Classroom(Data(classroomId)),
        )

        data class Data(var id: Int)

        data class Syllabus(var data: Data)
        data class Lecturer(var data: Data)
        data class Discipline(var data: Data)
        data class Group(var data: Data)
        data class Classroom(var data: Data)
    }

    companion object {
        val columnNameIds = listOf(
            R.string.column_week,
            R.string.column_day,
            R.string.column_period,
        )
        val weekNameIdsShort = listOf(
            R.string.even_short,
            R.string.odd_short,
        )
        val weekNameIds = listOf(
            R.string.even,
            R.string.odd,
        )
        val dayNameIdsShort = listOf(
            R.string.mon,
            R.string.tue,
            R.string.wed,
            R.string.thu,
            R.string.fri,
            R.string.sat,
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
            R.string.lection,
            R.string.lab,
            R.string.practice,
            R.string.isw,
        )
    }
}
