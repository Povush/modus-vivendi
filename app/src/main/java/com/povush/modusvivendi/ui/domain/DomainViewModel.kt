package com.povush.modusvivendi.ui.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.povush.modusvivendi.data.model.WeatherResponse
import com.povush.modusvivendi.data.network.openweather.WeatherApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DomainViewModel @Inject constructor(
    private val weatherApiService: WeatherApiService
) : ViewModel() {
    val weatherResponse = MutableStateFlow(WeatherResponse())

    init {
        viewModelScope.launch {
            weatherResponse.value = weatherApiService.getWeather(
                lat = 55.620189,
                lon = 37.760022
            )
        }
    }
}