package com.example.auddistandroid.service

import com.example.auddistandroid.data.model.DataList
import com.example.auddistandroid.data.model.auth.AuthToken
import com.example.auddistandroid.data.model.entity.Lecturer
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AudDistApi {

    @POST("/auth/token/login/")
    suspend fun getToken(@Body requestBody: RequestBody): AuthToken

    @GET("/api/lecturers/")
    suspend fun getLecturers(): DataList<Lecturer>

//    @GET("/api/disciplines/")
//    suspend fun getDisciplines(): DataList<Discipline>
}