package com.example.auddistandroid.api

import com.example.auddistandroid.data.model.LecturersList
import retrofit2.http.GET
import retrofit2.http.Headers

interface AudDistApi {
    companion object {
        const val BASE_URL = "http://192.168.0.9:8000/api/"
        const val CONTENT_TYPE = "Content-Type: application/vnd.api+json"
        private const val TOKEN = "1f0f51aec8a80397acd4a1b78bf69336158adea9" // tempo
        private const val AUTH_TOKEN = "Authorization: Token $TOKEN"
    }

    @Headers(CONTENT_TYPE, AUTH_TOKEN)
    @GET("lecturers")
    suspend fun getLecturers(): LecturersList
}