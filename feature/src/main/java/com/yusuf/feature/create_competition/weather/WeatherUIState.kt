package com.yusuf.feature.create_competition.weather

import com.yusuf.domain.model.weather.CurrentWeatherModel

data class WeatherUIState(
    val isLoading: Boolean = false,
    val currentWeather: CurrentWeatherModel? = null,
    val error: String? = null
)