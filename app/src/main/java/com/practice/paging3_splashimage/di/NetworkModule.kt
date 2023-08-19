package com.practice.paging3_splashimage.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.practice.paging3_splashimage.data.remote.UnsplashApi
import com.practice.paging3_splashimage.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    // to define how to provide a retrofit instance
    @Provides
    @Singleton // have a singleton...
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    /*
    .readTimeout(15, TimeUnit.SECONDS)는 읽기 작업의 타임아웃을 15초로 설정하는 메서드입니다.
    이는 서버로부터 데이터를 읽어오는데 최대 15초까지 대기할 수 있다는 의미입니다.

    .connectTimeout(15, TimeUnit.SECONDS)는 연결 작업의 타임아웃을 15초로 설정하는 메서드입니다.
    이는 서버에 연결을 시도하는데 최대 15초까지 대기할 수 있다는 의미입니다.
     */

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val contentType = "application/json".toMediaType()
        val json = Json {
            // specifies whether encounters of unknown properties in the input JSON should be ignored,
            // instead of exception(SerializationException)
            ignoreUnknownKeys = true
        }
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            // which library to use for "de"serialization(JSON -> Object)
            // kotlinx-serialization dependency
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    @Provides
    @Singleton
    fun provideUnsplashApi(retrofit: Retrofit): UnsplashApi {
        return retrofit.create(UnsplashApi::class.java)
    }
}