package com.povush.modusvivendi.data.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.povush.modusvivendi.BuildConfig
import com.povush.modusvivendi.data.network.ApiKeyInterceptor
import com.povush.modusvivendi.data.network.openweather.WeatherApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WeatherModule {
    private const val BASE_URL = "https://api.openweathermap.org/"

    object JsonConfig {
        val instance: Json = Json {
            ignoreUnknownKeys = true
        }
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(ApiKeyInterceptor(BuildConfig.OPENWEATHER_API_KEY))
            .build()

        return Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(JsonConfig.instance.asConverterFactory("application/json".toMediaType()))
            .baseUrl(BASE_URL)
            .build()
    }

    @Provides
    @Singleton
    fun provideWeatherApiService(retrofit: Retrofit): WeatherApiService {
        return retrofit.create(WeatherApiService::class.java)
    }
}