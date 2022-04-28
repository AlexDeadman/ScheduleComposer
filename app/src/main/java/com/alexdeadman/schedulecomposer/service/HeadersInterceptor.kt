package com.alexdeadman.schedulecomposer.service

import android.content.SharedPreferences
import com.alexdeadman.schedulecomposer.App.Companion.preferences
import com.alexdeadman.schedulecomposer.util.Keys
import okhttp3.Interceptor
import okhttp3.Response

class HeadersInterceptor : Interceptor, SharedPreferences.OnSharedPreferenceChangeListener {

    init {
        preferences.registerOnSharedPreferenceChangeListener(this)
    }

    private var authToken: String? = preferences.getString(Keys.AUTH_TOKEN, null)

    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
            .header("Content-Type", "application/vnd.api+json")
        if (authToken != null) {
            builder.header("Authorization", "Token $authToken")
        }
        return chain.proceed(builder.build())
    }

    override fun onSharedPreferenceChanged(sp: SharedPreferences, key: String?) {
        if (key == Keys.AUTH_TOKEN) {
            authToken = sp.getString(key, null)
        }
    }
}