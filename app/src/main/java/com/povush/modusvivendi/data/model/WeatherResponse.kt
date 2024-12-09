package com.povush.modusvivendi.data.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherResponse(
    @SerializedName("main") val main: Main = Main(),
    @SerializedName("weather") val weather: List<Weather> = emptyList(),
    @SerializedName("name") val cityName: String = "No city"
)

@Serializable
data class Main(
    @SerializedName("temp") val temp: Double = 0.00
)

@Serializable
data class Weather(
    @SerializedName("description") val description: String = "Empty"
)