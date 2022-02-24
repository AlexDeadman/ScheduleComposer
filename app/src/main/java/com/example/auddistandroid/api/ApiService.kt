package com.example.auddistandroid.api

import com.example.auddistandroid.App.Companion.preferences
import com.example.auddistandroid.data.model.*
import com.example.auddistandroid.data.model.auth.AuthToken
import com.example.auddistandroid.data.model.entity.Discipline
import com.example.auddistandroid.data.model.entity.Lecturer
import com.example.auddistandroid.utils.Keys
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {
    companion object {
        var BASE_URL = preferences.getString(Keys.BASE_URL, "")!!
    }

    @POST
    suspend fun getToken(
        @Body requestBody: RequestBody,
        @Url url: String = "$BASE_URL/auth/token/login/"
    ): AuthToken

    @GET
    suspend fun getLecturers(
        @Header("Authorization") authToken: String,
        @Url url: String = "$BASE_URL/api/lecturers/"
    ): DataList<Lecturer>

    @GET
    suspend fun getDisciplines(
        @Header("Authorization") authToken: String,
        @Url url: String = "$BASE_URL/api/disciplines/"
    ): DataList<Discipline>
}