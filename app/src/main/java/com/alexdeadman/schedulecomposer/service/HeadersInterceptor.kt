package com.alexdeadman.schedulecomposer.service

import android.content.SharedPreferences
import com.alexdeadman.schedulecomposer.App.Companion.preferences
import com.alexdeadman.schedulecomposer.util.key.PreferenceKeys
import okhttp3.Interceptor
import okhttp3.Response

class HeadersInterceptor : Interceptor, SharedPreferences.OnSharedPreferenceChangeListener {

    init {
        preferences.registerOnSharedPreferenceChangeListener(this)
    }

    private var authToken: String? = preferences.getString(PreferenceKeys.AUTH_TOKEN, null)

    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain
            .request()
            .newBuilder()
            .header("Content-Type", "application/vnd.api+json")
        authToken?.let {
            builder.header("Authorization", "Token $it")
        }
        return chain.proceed(builder.build())
    }

    override fun onSharedPreferenceChanged(sp: SharedPreferences, key: String?) {
        if (key == PreferenceKeys.AUTH_TOKEN) {
            authToken = sp.getString(key, null)
        }
    }
}