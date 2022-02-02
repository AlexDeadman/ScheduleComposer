package com.example.auddistandroid.data.model

import com.google.gson.annotations.SerializedName

data class GroupList(var data: List<Group>) {

    data class Group(
        var type: String,
        var id: Int,
        var attributes: Attributes,
        var relationships: Relationships
    ) {

        data class Attributes(
            var number: String,
            @SerializedName("students_count") var studentsCount: Int
        )

        data class Relationships(var syllabus: Syllabus) {

            data class Syllabus(var data: Data) {

                data class Data(var id: Int)
            }
        }
    }
}
