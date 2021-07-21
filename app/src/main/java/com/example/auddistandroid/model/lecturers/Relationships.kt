package com.example.auddistandroid.model.lecturers

import com.google.gson.annotations.SerializedName

data class Relationships(
    @SerializedName("disciplines") var disciplines : Disciplines
)

data class Disciplines (
    @SerializedName("data") var data : List<Data>
)

data class Data (
    @SerializedName("type") var type : String,
    @SerializedName("id") var id : String
)
