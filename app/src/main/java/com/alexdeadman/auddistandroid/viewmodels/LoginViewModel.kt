package com.alexdeadman.auddistandroid.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alexdeadman.auddistandroid.App.Companion.preferences
import com.alexdeadman.auddistandroid.data.model.auth.LoginData
import com.alexdeadman.auddistandroid.service.AudDistApi
import com.alexdeadman.auddistandroid.utils.Keys
import com.alexdeadman.auddistandroid.utils.state.LoginState
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val audDistApi: AudDistApi
) : ViewModel() {

    private val _state = MutableLiveData<LoginState>()
    val state: LiveData<LoginState> get() = _state

    fun fetchToken(username: String, password: String) {
        _state.value = LoginState.Sending

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val loginData = LoginData(
                    LoginData.Data(
                        "TokenCreateView",
                        LoginData.Data.Attributes(username, password)
                    )
                )

                val authToken = audDistApi.getToken(
                    Gson().toJson(loginData).toRequestBody()
                ).data.attributes.authToken

                preferences.edit().apply {
                    putString(Keys.AUTH_TOKEN, authToken)
                    putString(Keys.USERNAME, username)
                    apply()
                }

                _state.postValue(LoginState.Success)

            } catch (e: Exception) {
                _state.postValue(LoginState.Error(e.message.toString()))
            }
        }
    }
}