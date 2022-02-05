package com.example.auddistandroid.data

import com.example.auddistandroid.api.AudDistApi
import com.example.auddistandroid.data.model.DataList
import com.example.auddistandroid.data.model.Discipline
import com.example.auddistandroid.data.model.Lecturer
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Header
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.reflect.typeOf

@Singleton
class AudDistRepository @Inject constructor(
    private val audDistApi: AudDistApi
) {
    suspend fun getToken(@Body requestBody: RequestBody) =
        audDistApi.getToken(requestBody)

    suspend fun getLecturers(authToken: String) =
        audDistApi.getLecturers(authToken)

//    suspend fun getDisciplines(@Header("Authorization") authToken: String) =
//        audDistApi.getDisciplines(authToken)
//
//    suspend fun getSchedules(@Header("Authorization") authToken: String) =
//        audDistApi.getSchedules(authToken)
}