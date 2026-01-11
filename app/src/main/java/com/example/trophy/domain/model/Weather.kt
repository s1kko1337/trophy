package com.example.trophy.domain.model

/**
 * Domain модель погодных условий.
 */
data class Weather(
    val id: Long = 0,
    val temperature: Int? = null,
    val pressure: Int? = null,
    val humidity: Int? = null,
    val windSpeed: Double? = null,
    val windDirection: WindDirection? = null,
    val cloudiness: Cloudiness? = null,
    val precipitation: Precipitation? = null,
    val moonPhase: MoonPhase? = null
)
