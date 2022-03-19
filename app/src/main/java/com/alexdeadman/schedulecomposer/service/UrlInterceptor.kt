package com.alexdeadman.schedulecomposer.service

import android.content.SharedPreferences
import com.alexdeadman.schedulecomposer.App.Companion.preferences
import com.alexdeadman.schedulecomposer.utils.Keys
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.Response

class UrlInterceptor : Interceptor, SharedPreferences.OnSharedPreferenceChangeListener {

    init {
        preferences.registerOnSharedPreferenceChangeListener(this)
    }

    var httpUrl: HttpUrl? = preferences.getString(Keys.URL, null)?.toHttpUrlOrNull()

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        return chain.proceed(
            if (httpUrl != null) {
                request.newBuilder()
                    .url(httpUrl!!.let {
                        // only way to change URL at runtime
                        request.url.newBuilder()
                            .scheme(it.scheme)
                            .host(it.host)
                            .port(it.port)
                            .build()
                    })
                    .build()
            } else {
                request
            }
        )
    }

    override fun onSharedPreferenceChanged(sp: SharedPreferences, key: String?) {
        if (key == Keys.URL) {
            httpUrl = sp.getString(key, null)?.toHttpUrlOrNull()
        }
    }
}