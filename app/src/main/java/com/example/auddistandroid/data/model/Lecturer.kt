package com.example.auddistandroid.data.model

import com.google.gson.annotations.SerializedName


data class Lecturer(
    var type: String,
    var id: Int,
    var attributes: LecturerAttributes,
    var relationships: LecturerRelationships
) : Entity {

    data class LecturerAttributes(
        @SerializedName("first_name") var firstName: String,
        var surname: String,
        var patronymic: String
    ) : Attributes

    data class LecturerRelationships(var disciplines: Disciplines) : Relationships {

        data class Disciplines(var data: List<Data>) {

            data class Data(var id: Int)
        }
    }
}

