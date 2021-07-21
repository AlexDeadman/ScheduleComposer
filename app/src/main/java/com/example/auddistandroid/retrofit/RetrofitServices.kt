package com.example.auddistandroid.retrofit

import com.example.auddistandroid.model.lecturers.LecturersList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers

interface RetrofitServices {
    @Headers(
        "Content-Type: application/vnd.api+json",
        "Authorization: Token 0e807cdb657c25b0ff2fffb50d7c4a849c3d38e0"
    )
    @GET("lecturers")
    fun getLecturersCall(): Call<LecturersList>
}