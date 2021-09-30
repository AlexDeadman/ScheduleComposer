package com.example.auddistandroid.api

import com.example.auddistandroid.data.model.LecturersList
import retrofit2.http.GET
import retrofit2.http.Headers

interface AudDistApi {
    companion object {
        const val BASE_URL = "http://192.168.255.122:8000/api/"
        const val AUTH_TOKEN = "8fa0306d73ef5643b314e0b12ba29e4fe0d3fcab" // tempo
    }

    @Headers(
        "Content-Type: application/vnd.api+json",
        "Authorization: Token $AUTH_TOKEN"
    )
    @GET("lecturers")
    suspend fun getLecturers(): LecturersList
}