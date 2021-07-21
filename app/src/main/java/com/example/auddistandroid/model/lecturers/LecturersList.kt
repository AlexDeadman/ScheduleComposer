package com.example.auddistandroid.model.lecturers

import com.google.gson.annotations.SerializedName

data class LecturersList(
    @SerializedName("data") var data : List<Lecturer>,
)
