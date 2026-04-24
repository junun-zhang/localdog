package com.calsync.app.data.remote.model
data class WeatherDto(
    val city: String, val temperature: Float, val feelsLike: Float,
    val weatherCode: Int, val weatherText: String, val humidity: Int,
    val windSpeed: Float, val updateTime: Long, val daily: List<DailyWeatherDto>?
)
data class DailyWeatherDto(
    val date: Long, val weatherCode: Int,
    val tempHigh: Float, val tempLow: Float, val weatherText: String
)
