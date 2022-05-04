package com.alexdeadman.schedulecomposer.service

import android.content.SharedPreferences
import com.alexdeadman.schedulecomposer.App.Companion.preferences
import com.alexdeadman.schedulecomposer.util.key.PreferenceKeys
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.Response

class UrlInterceptor : Interceptor, SharedPreferences.OnSharedPreferenceChangeListener {

    init {
        preferences.registerOnSharedPreferenceChangeListener(this)
    }

    private var httpUrl: HttpUrl? = preferences
        .getString(PreferenceKeys.URL, null)?.toHttpUrlOrNull()

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        httpUrl?.let {
            request = request.newBuilder()
                .url(
                    request.url.newBuilder()
                        .scheme(it.scheme)
                        .host(it.host)
                        .port(it.port)
                        .build()
                )
                .build()
        }
        return chain.proceed(request)
    }

    override fun onSharedPreferenceChanged(sp: SharedPreferences, key: String?) {
        if (key == PreferenceKeys.URL) {
            httpUrl = sp.getString(key, null)?.toHttpUrlOrNull()
        }
    }
}