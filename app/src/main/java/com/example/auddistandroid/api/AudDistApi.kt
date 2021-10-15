package com.example.auddistandroid.api

import com.example.auddistandroid.data.model.AuthToken
import com.example.auddistandroid.data.model.LecturersList
import okhttp3.RequestBody
import retrofit2.http.*

interface AudDistApi {
    companion object {
        const val CONTENT_TYPE = "Content-Type: application/vnd.api+json"
//        const val BASE_URL = "http://192.168.0.9:8000/"
        const val BASE_URL = "http://192.168.0.7:8000/"
//        const val BASE_URL = "http://192.168.61.122:8000/"
    }

    @Headers(CONTENT_TYPE)
    @POST("auth/token/login/")
    suspend fun getToken(@Body requestBody: RequestBody): AuthToken

    @Headers(CONTENT_TYPE)
    @GET("api/lecturers/")
    suspend fun getLecturers(@Header("Authorization") authToken: String): LecturersList
}