package com.example.auddistandroid.data.model.entities

import com.bin.david.form.annotation.ColumnType
import com.bin.david.form.annotation.SmartColumn
import com.bin.david.form.annotation.SmartTable
import com.google.gson.annotations.SerializedName


@SmartTable
data class Schedule(
    var type: String,
    var id: Int,

    @SmartColumn(type = ColumnType.Child)
    var attributes: Attributes,

    @SmartColumn(type = ColumnType.Child)
    var relationships: Relationships,
) : Entity {

    data class Attributes(
        @SmartColumn
        @SerializedName("lecture_type")
        var lectureType: Int,

        @SmartColumn
        var semester: Int,

        @SmartColumn
        @SerializedName("week_parity")
        var weekParity: Boolean,

        @SmartColumn
        @SerializedName("day_of_the_week")
        var dayOfTheWeek: Int,

        @SmartColumn
        @SerializedName("lecture_begin")
        var lectureBegin: Int,
    )

    data class Relationships(
        @SmartColumn(type = ColumnType.Child)
        var lecturer: Lecturer,

        @SmartColumn(type = ColumnType.Child)
        var discipline: Discipline,

        @SmartColumn(type = ColumnType.Child)
        var group: Group,

        @SmartColumn(type = ColumnType.Child)
        var audience: Audience,
    ) {

        data class Data(@SmartColumn var id: Int)

        data class Lecturer(@SmartColumn(type = ColumnType.Child) var data: Data)
        data class Discipline(@SmartColumn(type = ColumnType.Child) var data: Data)
        data class Group(@SmartColumn(type = ColumnType.Child) var data: Data)
        data class Audience(@SmartColumn(type = ColumnType.Child) var data: Data)
    }
}
