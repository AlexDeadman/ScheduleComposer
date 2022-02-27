package com.example.auddistandroid.service

import okhttp3.Interceptor
import okhttp3.Response

class HeadersInterceptor : Interceptor {

    var authToken: String? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
            .header("Content-Type", "application/vnd.api+json")
        if (authToken != null) {
            builder.header("Authorization", "Token $authToken")
        }
        return chain.proceed(builder.build())
    }
}