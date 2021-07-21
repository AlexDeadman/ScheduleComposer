package com.example.auddistandroid.model.lecturers

import com.google.gson.annotations.SerializedName

data class Lecturer(
    @SerializedName("type") var type : String,
    @SerializedName("id") var id : String,
    @SerializedName("attributes") var attributes : Attributes,
    @SerializedName("relationships") var relationships : Relationships
)
