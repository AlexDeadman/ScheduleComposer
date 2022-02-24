package com.example.auddistandroid.di

import com.example.auddistandroid.api.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor {
                return@addInterceptor it.proceed(
                    it.request().newBuilder()
                        .header("Content-Type", "application/vnd.api+json")
                        .build()
                )
            }.build()

    @Provides
    @Singleton
    fun provideRetrofit(httpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("http://template/") // required
            .client(httpClient)
            .build()


    @Provides
    @Singleton
    fun provideAudDistApi(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)
}