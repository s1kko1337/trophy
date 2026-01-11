package com.example.trophy.data.repository

import com.example.trophy.data.local.database.dao.WeatherDao
import com.example.trophy.data.mapper.WeatherMapper
import com.example.trophy.domain.model.Weather
import com.example.trophy.domain.repository.WeatherRepository
import javax.inject.Inject

/**
 * Реализация репозитория для работы с погодными условиями.
 */
class WeatherRepositoryImpl @Inject constructor(
    private val weatherDao: WeatherDao,
    private val mapper: WeatherMapper
) : WeatherRepository {

    override suspend fun getWeatherById(id: Long): Weather? {
        return weatherDao.getWeatherById(id)?.let { mapper.toDomain(it) }
    }

    override suspend fun insertWeather(weather: Weather): Long {
        return weatherDao.insert(mapper.toEntity(weather))
    }

    override suspend fun updateWeather(weather: Weather) {
        weatherDao.update(mapper.toEntity(weather))
    }

    override suspend fun deleteWeather(weather: Weather) {
        weatherDao.delete(mapper.toEntity(weather))
    }

    override suspend fun deleteWeatherById(id: Long) {
        weatherDao.deleteById(id)
    }
}
