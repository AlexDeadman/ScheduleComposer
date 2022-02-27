package com.example.auddistandroid.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.auddistandroid.App.Companion.preferences
import com.example.auddistandroid.data.AudDistRepository
import com.example.auddistandroid.data.model.auth.LoginData
import com.example.auddistandroid.service.HeadersInterceptor
import com.example.auddistandroid.utils.Keys
import com.example.auddistandroid.utils.state.LoginState
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AudDistRepository,
    private val headersInterceptor: HeadersInterceptor
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
                        LoginData.Data.Attributes(username, password) // TODO validation
                    )
                )

                val authToken = repository.getToken(
                    Gson().toJson(loginData).toRequestBody()
                ).data.attributes.authToken

                preferences.edit().apply {
                    putString(Keys.AUTH_TOKEN, authToken)
                    putString(Keys.USERNAME, username)
                    apply()
                }

                headersInterceptor.authToken = authToken

                _state.postValue(LoginState.Success)

            } catch (e: Exception) {
                _state.postValue(LoginState.Error(e.message.toString()))
            }
        }
    }
}