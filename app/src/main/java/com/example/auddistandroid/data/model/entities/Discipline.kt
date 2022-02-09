package com.example.auddistandroid.data.model.entities

import com.google.gson.annotations.SerializedName


data class Discipline(
    var type: String,
    var id: Int,
    var attributes: Attributes,
    var relationships: Relationships
) {

    data class Attributes(
        var name: String,
        var code: String,
        var cycle: String,
        @SerializedName("hours_total") var hoursTotal: Int,
        @SerializedName("hours_lec") var hoursLec: Int,
        @SerializedName("hours_pr") var hoursPr: Int,
        @SerializedName("hours_la") var hoursLa: Int,
        @SerializedName("hours_isw") var hoursIsw: Int,
        @SerializedName("hours_cons") var hoursCons: Int
    )

    data class Relationships(var syllabus: Syllabus) {

        data class Syllabus(var data: Data) {

            data class Data(var id: Int)
        }
    }
}

