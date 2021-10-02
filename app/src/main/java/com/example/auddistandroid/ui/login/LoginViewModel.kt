package com.example.auddistandroid.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.auddistandroid.api.AudDistApi
import com.example.auddistandroid.data.AudDistRepository
import com.example.auddistandroid.data.model.AuthToken
import com.example.auddistandroid.ui.QueryStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AudDistRepository,
): ViewModel() {

    lateinit var username: String
    lateinit var password: String

    lateinit var queryStatus: QueryStatus

    val token = liveData(Dispatchers.IO) {
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
            queryStatus = QueryStatus.SUCCESS
        } catch (e: Exception) {
            queryStatus = when (e) {
                is ConnectException -> QueryStatus.NO_INTERNET // TODO ConnectException не подходит
                is HttpException -> QueryStatus.UNAUTHORIZED // TODO HttpException возможно тоже не подходит
                is SocketTimeoutException -> QueryStatus.NO_RESPONSE
                else -> QueryStatus.UNKNOWN_ERROR
            }
        } finally {
            emit(authToken)
        }

    }
}