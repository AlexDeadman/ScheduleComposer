package com.example.auddistandroid.api

import com.example.auddistandroid.App.Companion.preferences
import com.example.auddistandroid.data.model.*
import com.example.auddistandroid.data.model.entities.Lecturer
import com.example.auddistandroid.utils.Keys
import okhttp3.RequestBody
import retrofit2.http.*

interface AudDistApi {
    companion object {
        const val CONTENT_TYPE = "Content-Type: application/vnd.api+json"
        var BASE_URL = preferences.getString(Keys.BASE_URL, "")!!
    }

    @POST
    @Headers(CONTENT_TYPE)
    suspend fun getToken(
        @Body requestBody: RequestBody,
        @Url url: String = "$BASE_URL/auth/token/login/"
    ): AuthToken

    @GET
    @Headers(CONTENT_TYPE)
    suspend fun getLecturers(
        @Header("Authorization") authToken: String,
        @Url url: String = "$BASE_URL/api/lecturers/"
    ): DataList<Lecturer>
}