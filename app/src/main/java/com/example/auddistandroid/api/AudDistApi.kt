package com.example.auddistandroid.api

import com.example.auddistandroid.data.model.LecturersList
import retrofit2.http.GET
import retrofit2.http.Headers

interface AudDistApi {
    companion object {
        const val BASE_URL = "http://192.168.0.9:8000/api/"
        const val AUTH_TOKEN = "0e807cdb657c25b0ff2fffb50d7c4a849c3d38e0" // tempo
    }

    @Headers(
        "Content-Type: application/vnd.api+json",
        "Authorization: Token $AUTH_TOKEN"
    )
    @GET("lecturers")
    suspend fun getLecturers(): LecturersList
}