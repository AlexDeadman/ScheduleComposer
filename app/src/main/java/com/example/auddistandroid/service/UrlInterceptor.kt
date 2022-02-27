package com.example.auddistandroid.service

import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response

class UrlInterceptor : Interceptor {

    var httpUrl: HttpUrl? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        return chain.proceed(
            if (httpUrl != null) {
                request.newBuilder()
                    .url(httpUrl!!.let {
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
}