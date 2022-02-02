package com.example.auddistandroid.data.model

import com.google.gson.annotations.SerializedName

data class LecturerList(var data: List<Lecturer>) {

    data class Lecturer(
        var type: String,
        var id: Int,
        var attributes: Attributes,
        var relationships: Relationships
    ) {

        data class Attributes(
            @SerializedName("first_name") var firstName: String,
            var surname: String,
            var patronymic: String
        )

        data class Relationships(var disciplines: Disciplines) {

            data class Disciplines(var data: List<Data>) {

                data class Data(var id: Int)
            }
        }
    }
}
