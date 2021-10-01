package com.example.auddistandroid.data

import com.example.auddistandroid.api.AudDistApi
import okhttp3.RequestBody
import retrofit2.http.Body
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudDistRepository @Inject constructor(
    private val audDistApi: AudDistApi
) {
    suspend fun getLecturers() = audDistApi.getLecturers()
    suspend fun getToken(@Body requestBody: RequestBody) = audDistApi.getToken(requestBody)
}