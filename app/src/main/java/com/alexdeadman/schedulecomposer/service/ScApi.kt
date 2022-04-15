package com.alexdeadman.schedulecomposer.service

import com.alexdeadman.schedulecomposer.model.DataList
import com.alexdeadman.schedulecomposer.model.auth.AuthToken
import com.alexdeadman.schedulecomposer.model.entity.*
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ScApi {

    companion object {
        private const val CLASSROOMS = "/api/classrooms/"
        private const val DIRECTIONS = "/api/directions/"
        private const val DISCIPLINES = "/api/disciplines/"
        private const val GROUPS = "/api/groups/"
        private const val LECTURERS = "/api/lecturers/"
        private const val SCHEDULES = "/api/schedules/"
        private const val SYLLABUSES = "/api/syllabuses/"
    }

    @POST("/auth/token/login/") suspend fun getToken(@Body rb: RequestBody): AuthToken

    @GET(CLASSROOMS) suspend fun getClassrooms(): DataList<Classroom>
    @POST(CLASSROOMS) suspend fun postClassroom(@Body rb: RequestBody)
    @PUT("$CLASSROOMS{id}/") suspend fun putClassroom(@Path("id") id: Int, @Body rb: RequestBody)
    @DELETE("$CLASSROOMS{id}/") suspend fun deleteClassroom(@Path("id") id: Int): Response<Unit>

    @GET(DIRECTIONS) suspend fun getDirections(): DataList<Direction>
    @POST(DIRECTIONS) suspend fun postDirection(@Body rb: RequestBody)
    @PUT("$DIRECTIONS{id}/") suspend fun putDirection(@Path("id") id: Int, @Body rb: RequestBody)
    @DELETE("$DIRECTIONS{id}/") suspend fun deleteDirection(@Path("id") id: Int): Response<Unit>

    @GET(DISCIPLINES) suspend fun getDisciplines(): DataList<Discipline>
    @POST(DISCIPLINES) suspend fun postDiscipline(@Body rb: RequestBody)
    @PUT("$DISCIPLINES{id}/") suspend fun putDiscipline(@Path("id") id: Int, @Body rb: RequestBody)
    @DELETE("$DISCIPLINES{id}/") suspend fun deleteDiscipline(@Path("id") id: Int): Response<Unit>

    @GET(GROUPS) suspend fun getGroups(): DataList<Group>
    @POST(GROUPS) suspend fun postGroup(@Body rb: RequestBody)
    @PUT("$GROUPS{id}/") suspend fun putGroup(@Path("id") id: Int, @Body rb: RequestBody)
    @DELETE("$GROUPS{id}/") suspend fun deleteGroup(@Path("id") id: Int): Response<Unit>

    @GET(LECTURERS) suspend fun getLecturers(): DataList<Lecturer>
    @POST(LECTURERS) suspend fun postLecturer(@Body rb: RequestBody)
    @PUT("$LECTURERS{id}/") suspend fun putLecturer(@Path("id") id: Int, @Body rb: RequestBody)
    @DELETE("$LECTURERS{id}/") suspend fun deleteLecturer(@Path("id") id: Int): Response<Unit>

    @GET(SCHEDULES) suspend fun getSchedule(): DataList<Schedule>
    @POST(SCHEDULES) suspend fun postSchedule(@Body rb: RequestBody)
    @PUT("$SCHEDULES{id}/") suspend fun putSchedule(@Path("id") id: Int, @Body rb: RequestBody)
    @DELETE("$SCHEDULES{id}/") suspend fun deleteSchedule(@Path("id") id: Int): Response<Unit>

    @GET(SYLLABUSES) suspend fun getSyllabuses(): DataList<Syllabus>
    @POST(SYLLABUSES) suspend fun postSyllabus(@Body rb: RequestBody)
    @PUT("$SYLLABUSES{id}/") suspend fun putSyllabus(@Path("id") id: Int, @Body rb: RequestBody)
    @DELETE("$SYLLABUSES{id}/") suspend fun deleteSyllabus(@Path("id") id: Int): Response<Unit>

}