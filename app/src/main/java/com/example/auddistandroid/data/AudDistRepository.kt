package com.example.auddistandroid.data

import com.example.auddistandroid.api.AudDistApi
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudDistRepository @Inject constructor(
    private val audDistApi: AudDistApi
) {
    suspend fun getToken(requestBody: RequestBody) = audDistApi.getToken(requestBody)

    suspend fun getLecturers(authToken: String) = audDistApi.getLecturers(authToken)

//    suspend fun getDisciplines(@Header("Authorization") authToken: String) =
//        audDistApi.getDisciplines(authToken)
//
//    suspend fun getSchedules(@Header("Authorization") authToken: String) =
//        audDistApi.getSchedules(authToken)
}