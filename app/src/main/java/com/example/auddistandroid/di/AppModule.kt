package com.example.auddistandroid.di

import com.example.auddistandroid.api.AudDistApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(AudDistApi.BASE_URL)
            .build()

    @Provides
    @Singleton
    fun provideAudDistApi(retrofit: Retrofit): AudDistApi =
        retrofit.create(AudDistApi::class.java)

}