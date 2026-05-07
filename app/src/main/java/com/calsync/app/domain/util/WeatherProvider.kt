package com.calsync.app.domain.util

import com.calsync.app.domain.model.Weather
import java.util.Calendar
import kotlin.random.Random

object WeatherProvider {

    fun getWeatherForDate(timestamp: Long): Weather {
        val calendar = Calendar.getInstance().apply { timeInMillis = timestamp }
        val month = calendar.get(Calendar.MONTH)
        val dayOfYear = calendar.get(Calendar.DAY_OF_YEAR)
        val seed = dayOfYear.toLong()
        val random = Random(seed)

        val season = getSeason(month)
        val (minTemp, maxTemp) = when (season) {
            Season.SUMMER -> 25 to 40
            Season.WINTER -> -10 to 10
            Season.SPRING, Season.AUTUMN -> 8 to 25
        }

        val temperature = random.nextInt(minTemp, maxTemp + 1)
        val condition = getConditionForSeason(season, random)
        val icon = getIconForCondition(condition)
        val humidity = if (condition == Weather.Condition.RAINY || condition == Weather.Condition.STORMY || condition == Weather.Condition.FOGGY) {
            random.nextInt(60, 100)
        } else {
            random.nextInt(20, 65)
        }
        val windSpeed = if (condition == Weather.Condition.STORMY || condition == Weather.Condition.WINDY) {
            random.nextInt(30, 80)
        } else {
            random.nextInt(0, 25)
        }

        return Weather(
            temperature = temperature,
            condition = condition,
            city = "New York",
            icon = icon,
            humidity = humidity,
            windSpeed = windSpeed
        )
    }

    fun getWeekWeather(weekStart: Long): List<Weather> {
        val oneDayMs = 24 * 60 * 60 * 1000L
        return (0 until 7).map { dayOffset ->
            getWeatherForDate(weekStart + dayOffset * oneDayMs)
        }
    }

    private fun getSeason(month: Int): Season {
        return when (month) {
            Calendar.DECEMBER, Calendar.JANUARY, Calendar.FEBRUARY -> Season.WINTER
            Calendar.MARCH, Calendar.APRIL, Calendar.MAY -> Season.SPRING
            Calendar.JUNE, Calendar.JULY, Calendar.AUGUST -> Season.SUMMER
            Calendar.SEPTEMBER, Calendar.OCTOBER, Calendar.NOVEMBER -> Season.AUTUMN
            else -> Season.SPRING
        }
    }

    private fun getConditionForSeason(season: Season, random: Random): Weather.Condition {
        val conditions = when (season) {
            Season.SUMMER -> listOf(
                Weather.Condition.SUNNY to 0.5f,
                Weather.Condition.CLOUDY to 0.2f,
                Weather.Condition.STORMY to 0.1f,
                Weather.Condition.RAINY to 0.1f,
                Weather.Condition.WINDY to 0.1f
            )
            Season.WINTER -> listOf(
                Weather.Condition.SNOWY to 0.35f,
                Weather.Condition.CLOUDY to 0.25f,
                Weather.Condition.SUNNY to 0.15f,
                Weather.Condition.WINDY to 0.1f,
                Weather.Condition.FOGGY to 0.1f,
                Weather.Condition.RAINY to 0.05f
            )
            Season.SPRING -> listOf(
                Weather.Condition.CLOUDY to 0.25f,
                Weather.Condition.RAINY to 0.2f,
                Weather.Condition.SUNNY to 0.2f,
                Weather.Condition.WINDY to 0.15f,
                Weather.Condition.FOGGY to 0.1f,
                Weather.Condition.STORMY to 0.1f
            )
            Season.AUTUMN -> listOf(
                Weather.Condition.CLOUDY to 0.25f,
                Weather.Condition.RAINY to 0.2f,
                Weather.Condition.WINDY to 0.2f,
                Weather.Condition.SUNNY to 0.15f,
                Weather.Condition.FOGGY to 0.1f,
                Weather.Condition.STORMY to 0.1f
            )
        }
        return weightedRandom(conditions, random)
    }

    private fun <T> weightedRandom(items: List<Pair<T, Float>>, random: Random): T {
        val totalWeight = items.sumOf { it.second.toDouble() }
        var value = random.nextDouble() * totalWeight
        for ((item, weight) in items) {
            value -= weight
            if (value <= 0) return item
        }
        return items.last().first
    }

    private fun getIconForCondition(condition: Weather.Condition): String {
        return when (condition) {
            Weather.Condition.SUNNY -> "\u2600\uFE0F"
            Weather.Condition.CLOUDY -> "\u2601\uFE0F"
            Weather.Condition.RAINY -> "\uD83C\uDF27\uFE0F"
            Weather.Condition.SNOWY -> "\u2744\uFE0F"
            Weather.Condition.STORMY -> "\u26C8\uFE0F"
            Weather.Condition.WINDY -> "\uD83C\uDF2C\uFE0F"
            Weather.Condition.FOGGY -> "\uD83C\uDF2B\uFE0F"
        }
    }

    private enum class Season {
        WINTER, SPRING, SUMMER, AUTUMN
    }
}
