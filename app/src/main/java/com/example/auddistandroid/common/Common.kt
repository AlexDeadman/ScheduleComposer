package com.example.auddistandroid.common

import com.example.auddistandroid.retrofit.RetrofitServices
import com.example.auddistandroid.retrofit.RetrofitClient

object Common {
    private const val BASE_URL = "http://192.168.0.5:8000/api/"
    val retrofitService: RetrofitServices
        get() = RetrofitClient.getClient(BASE_URL).create(RetrofitServices::class.java)
}