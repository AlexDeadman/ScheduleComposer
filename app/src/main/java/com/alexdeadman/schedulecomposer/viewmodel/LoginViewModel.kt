package com.alexdeadman.schedulecomposer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexdeadman.schedulecomposer.App.Companion.preferences
import com.alexdeadman.schedulecomposer.R
import com.alexdeadman.schedulecomposer.model.auth.LoginData
import com.alexdeadman.schedulecomposer.service.Api
import com.alexdeadman.schedulecomposer.util.key.PreferenceKeys
import com.alexdeadman.schedulecomposer.util.state.SendingState
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val api: Api,
) : ViewModel() {

    val state = MutableStateFlow<SendingState>(SendingState.Default())

    fun getToken(username: String, password: String) {
        state.value = SendingState.Sending()
        viewModelScope.launch(Dispatchers.IO) {
            state.value = try {
                val loginData = LoginData(
                    LoginData.Data(
                        "TokenCreateView",
                        LoginData.Data.Attributes(username, password)
                    )
                )
                val authToken = api.getToken(
                    Gson().toJson(loginData).toRequestBody()
                ).data.attributes.authToken

                preferences.edit().apply {
                    putString(PreferenceKeys.AUTH_TOKEN, authToken)
                    putString(PreferenceKeys.USERNAME, username)
                    apply()
                }
                SendingState.Success()
            } catch (e: Exception) {
                SendingState.Error(R.string.unknown_error)
            }
        }
        state.value = SendingState.Default()
    }
}