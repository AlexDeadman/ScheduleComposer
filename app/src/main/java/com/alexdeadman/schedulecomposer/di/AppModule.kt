package com.alexdeadman.schedulecomposer.di

import com.alexdeadman.schedulecomposer.service.Api
import com.alexdeadman.schedulecomposer.service.HeadersInterceptor
import com.alexdeadman.schedulecomposer.service.UrlInterceptor
import com.alexdeadman.schedulecomposer.viewmodel.ViewModelFactory
import com.google.gson.Gson
import com.google.gson.GsonBuilder
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
    fun provideHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(UrlInterceptor())
        .addInterceptor(HeadersInterceptor())
//        .addInterceptor(PlutoInterceptor())
        .build()

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().serializeNulls().create()

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson, client: OkHttpClient): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(gson))
        .baseUrl("http://template/") // required
        .client(client)
        .build()

    @Provides
    @Singleton
    fun provideScApi(retrofit: Retrofit): Api = retrofit.create(Api::class.java)

    @Provides
    @Singleton
    fun provideViewModelFactory(
        gson: Gson,
        api: Api,
    ): ViewModelFactory = ViewModelFactory(gson, api)
}