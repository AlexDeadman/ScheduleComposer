package com.example.auddistandroid.data.model.entity

import com.google.gson.annotations.SerializedName

data class Syllabus(
    var type: String,
    var id: Int,
    var attributes: Attributes,
    var relationships: Relationships,
) : Entity {

    data class Attributes(
        var year: String,
        @SerializedName("specialty_code") var specialtyCode: String,
        @SerializedName("specialty_name") var specialtyName: String,
    )

    data class Relationships(var direction: Direction) {

        data class Direction(var data: List<Data>) {

            data class Data(var id: Int)
        }
    }
}
