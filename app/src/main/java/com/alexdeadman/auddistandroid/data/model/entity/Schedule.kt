package com.alexdeadman.auddistandroid.data.model.entity

import com.google.gson.annotations.SerializedName


data class Schedule(
    var type: String,
    var id: Int,
    var attributes: Attributes,
    var relationships: Relationships,
) : Entity {

    data class Attributes(
        @SerializedName("lecture_type")
        var lectureType: Int,

        var semester: Int,

        @SerializedName("week_parity")
        var weekParity: Boolean,

        @SerializedName("day_of_the_week")
        var dayOfTheWeek: Int,

        @SerializedName("lecture_begin")
        var lectureBegin: Int,
    )

    data class Relationships(
        var lecturer: Lecturer,
        var discipline: Discipline,
        var group: Group,
        var audience: Audience,
    ) {
        data class Data(var id: Int)

        data class Lecturer(var data: Data)
        data class Discipline(var data: Data)
        data class Group(var data: Data)
        data class Audience(var data: Data)
    }
}
