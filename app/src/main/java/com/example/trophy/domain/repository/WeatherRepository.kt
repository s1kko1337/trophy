package com.example.trophy.domain.repository

import com.example.trophy.domain.model.Weather

/**
 * Интерфейс репозитория для работы с погодными условиями.
 */
interface WeatherRepository {

    suspend fun getWeatherById(id: Long): Weather?

    suspend fun insertWeather(weather: Weather): Long

    suspend fun updateWeather(weather: Weather)

    suspend fun deleteWeather(weather: Weather)

    suspend fun deleteWeatherById(id: Long)
}
