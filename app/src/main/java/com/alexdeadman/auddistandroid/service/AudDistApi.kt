package com.alexdeadman.auddistandroid.service

import com.alexdeadman.auddistandroid.data.model.DataList
import com.alexdeadman.auddistandroid.data.model.auth.AuthToken
import com.alexdeadman.auddistandroid.data.model.entity.*
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AudDistApi {

    @POST("/auth/token/login/")
    suspend fun getToken(@Body requestBody: RequestBody): AuthToken

    @GET("/api/lecturers/")
    suspend fun getLecturers(): DataList<Lecturer>

    @GET("/api/schedules/")
    suspend fun getSchedule(): DataList<Schedule>

    @GET("/api/disciplines/")
    suspend fun getDisciplines(): DataList<Discipline>

    @GET("/api/groups/")
    suspend fun getGroups(): DataList<Group>

    @GET("/api/audiences/")
    suspend fun getAudiences(): DataList<Audience>

    @GET("/api/directions/")
    suspend fun getDirections(): DataList<Direction>

    @GET("/api/syllabuses/")
    suspend fun getSyllabuses(): DataList<Syllabus>

}