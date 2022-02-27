package com.example.auddistandroid.data

import com.example.auddistandroid.service.AudDistApi
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudDistRepository @Inject constructor(
    private val audDistApi: AudDistApi
) {
    suspend fun getToken(requestBody: RequestBody) = audDistApi.getToken(requestBody)

    suspend fun getLecturers() = audDistApi.getLecturers()

//    suspend fun getDisciplines(authToken: String) = audDistApi.getDisciplines()
}