package com.alexdeadman.auddistandroid.data.model.entity

import com.google.gson.annotations.SerializedName

data class Syllabus(
    var type: String,
    var id: Int,
    var attributes: Attributes,
    var relationships: Relationships,
) : Entity {

    data class Attributes(
        var year: String,
        @SerializedName("specialty_code") var code: String,
        @SerializedName("specialty_name") var name: String,
    )

    data class Relationships(var direction: Direction) {

        data class Direction(var data: Data) {

            data class Data(var id: Int)
        }
    }
}
