package com.example.auddistandroid.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.auddistandroid.api.AudDistApi
import com.example.auddistandroid.data.AudDistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AudDistRepository,
): ViewModel() {

    lateinit var username: String
    lateinit var password: String

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

//        try {
            val token = repository.getToken(
                RequestBody.create(
                    MediaType.parse(AudDistApi.CONTENT_TYPE),
                    jsonObject.toString()
                )
            )
//        }

        emit(token.data.attributes.authToken)
    }
}