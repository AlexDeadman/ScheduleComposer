package com.example.auddistandroid.api

import com.example.auddistandroid.data.model.AuthToken
import com.example.auddistandroid.data.model.LecturersList
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface AudDistApi {
    companion object {
        const val CONTENT_TYPE = "Content-Type: application/vnd.api+json"

        const val BASE_URL = "http://192.168.235.122:8000/"
//        const val BASE_URL = "http://192.168.0.9:8000/"

        private const val TOKEN = "406b485327ee5d28c895c65c5e8352e08705b0a1"
//        private const val TOKEN = "1f0f51aec8a80397acd4a1b78bf69336158adea9"

        private const val AUTH_TOKEN = "Authorization: Token $TOKEN"
    }

    @Headers(CONTENT_TYPE)
    @POST("auth/token/login/")
    suspend fun getToken(@Body requestBody: RequestBody): AuthToken

    @Headers(CONTENT_TYPE, AUTH_TOKEN)
    @GET("api/lecturers")
    suspend fun getLecturers(): LecturersList
}