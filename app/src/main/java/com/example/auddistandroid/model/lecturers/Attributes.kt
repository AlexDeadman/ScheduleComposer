package com.example.auddistandroid.model.lecturers

import com.google.gson.annotations.SerializedName

data class Attributes(
    @SerializedName("first_name") var firstName : String,
    @SerializedName("surname") var surname : String,
    @SerializedName("patronymic") var patronymic : String
)
