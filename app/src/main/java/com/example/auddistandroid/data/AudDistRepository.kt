package com.example.auddistandroid.data

import com.example.auddistandroid.api.ApiService
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudDistRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getToken(requestBody: RequestBody) = apiService.getToken(requestBody)

    suspend fun getLecturers(authToken: String) = apiService.getLecturers(authToken)

    suspend fun getDisciplines(authToken: String) = apiService.getDisciplines(authToken)
}