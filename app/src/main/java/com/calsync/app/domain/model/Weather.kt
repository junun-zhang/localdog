package com.calsync.app.domain.model
data class Weather(
    val city: String, val temperature: Float, val feelsLike: Float,
    val weatherCode: Int, val weatherText: String, val humidity: Int,
    val windSpeed: Float, val updateTime: Long
) {
    fun getWeatherIconRes(): Int {
        return when (weatherCode) {
            0 -> R.drawable.ic_weather_sunny
            1, 2 -> R.drawable.ic_weather_cloudy
            3 -> R.drawable.ic_weather_overcast
            in 7..9 -> R.drawable.ic_weather_rainy
            in 10..14 -> R.drawable.ic_weather_snowy
            else -> R.drawable.ic_weather_cloudy
        }
    }
}
data class DailyWeather(
    val date: Long, val weatherCode: Int,
    val tempHigh: Float, val tempLow: Float, val weatherText: String
)
