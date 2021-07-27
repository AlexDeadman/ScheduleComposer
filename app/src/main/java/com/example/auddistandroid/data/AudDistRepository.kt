package com.example.auddistandroid.data

import com.example.auddistandroid.api.AudDistApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudDistRepository @Inject constructor(
    private val audDistApi: AudDistApi
) {
    suspend fun getLecturers() = audDistApi.getLecturers()
}