package com.calsync.app.domain.model

data class Weather(
    val temperature: Int,
    val condition: Condition,
    val city: String,
    val icon: String,
    val humidity: Int? = null,
    val windSpeed: Int? = null
) {
    enum class Condition {
        SUNNY, CLOUDY, RAINY, SNOWY, STORMY, WINDY, FOGGY
    }
}
