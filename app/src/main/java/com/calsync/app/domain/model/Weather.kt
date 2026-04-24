package com.calsync.app.domain.model

data class Weather(
    val city: String,
    val temperature: Float,
    val feelsLike: Float,
    val weatherCode: Int,
    val weatherText: String,
    val humidity: Int,
    val windSpeed: Float,
    val updateTime: Long
) {
    fun getWeatherIconName(): String {
        return when (weatherCode) {
            0 -> "sunny"
            1, 2 -> "cloudy"
            3 -> "overcast"
            in 7..9 -> "rainy"
            in 10..14 -> "snowy"
            else -> "cloudy"
        }
    }
}

data class DailyWeather(
    val date: Long,
    val weatherCode: Int,
    val tempHigh: Float,
    val tempLow: Float,
    val weatherText: String
)
