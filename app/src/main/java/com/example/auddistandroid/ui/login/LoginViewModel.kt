package com.example.auddistandroid.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.auddistandroid.api.AudDistApi
import com.example.auddistandroid.data.AudDistRepository
import com.example.auddistandroid.utils.ResponseStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.HttpException
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AudDistRepository,
): ViewModel() {

    lateinit var username: String
    lateinit var password: String

    lateinit var responseStatus: ResponseStatus

    val token = liveData(Dispatchers.IO) {
        // TODO избавится от этого кринжа
        val jsonObject = JSONObject(
            """
            {
                "data": {
                    "type": "TokenCreateView",
                    "attributes": {
                        "username": "$username",
                        "password": "$password"
                    }
                }
            }
            """.trimIndent()
        )

        var authToken = ""

        try {
            authToken = repository.getToken(
                RequestBody.create(
                    MediaType.parse(AudDistApi.CONTENT_TYPE),
                    jsonObject.toString()
                )
            ).data.attributes.authToken
            responseStatus = ResponseStatus.SUCCESS
        } catch (e: Exception) {
            responseStatus = when (e) { // TODO изменить способ обратоки ошибок, возможно нужен MVI
                is HttpException -> ResponseStatus.UNAUTHORIZED // TODO HttpException возможно не подходит
                is SocketTimeoutException -> ResponseStatus.NO_RESPONSE
                else -> ResponseStatus.UNKNOWN_ERROR
            }
        } finally {
            emit(authToken)
        }

    }
}