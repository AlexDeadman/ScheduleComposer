package com.alexdeadman.schedulecomposer.di

import com.alexdeadman.schedulecomposer.service.AudDistApi
import com.alexdeadman.schedulecomposer.service.HeadersInterceptor
import com.alexdeadman.schedulecomposer.service.UrlInterceptor
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
    fun provideUrlInterceptor(): UrlInterceptor = UrlInterceptor()

    @Provides
    @Singleton
    fun provideHeadersInterceptor(): HeadersInterceptor = HeadersInterceptor()

    @Provides
    @Singleton
    fun provideHttpClient(
        urlInterceptor: UrlInterceptor,
        headersInterceptor: HeadersInterceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(urlInterceptor)
            .addInterceptor(headersInterceptor)
//            .addInterceptor(PlutoInterceptor())
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("http://template/") // required
            .client(client)
            .build()

    @Provides
    @Singleton
    fun provideAudDistApi(retrofit: Retrofit): AudDistApi =
        retrofit.create(AudDistApi::class.java)
}