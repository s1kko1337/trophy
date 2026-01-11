package com.example.trophy.data.mapper

import com.example.trophy.data.local.database.entity.WeatherEntity
import com.example.trophy.domain.model.Weather
import javax.inject.Inject

/**
 * Маппер для конвертации между WeatherEntity и Weather domain model.
 */
class WeatherMapper @Inject constructor() {

    fun toDomain(entity: WeatherEntity): Weather {
        return Weather(
            id = entity.id,
            temperature = entity.temperature,
            pressure = entity.pressure,
            humidity = entity.humidity,
            windSpeed = entity.windSpeed,
            windDirection = entity.windDirection,
            cloudiness = entity.cloudiness,
            precipitation = entity.precipitation,
            moonPhase = entity.moonPhase
        )
    }

    fun toEntity(domain: Weather): WeatherEntity {
        return WeatherEntity(
            id = domain.id,
            temperature = domain.temperature,
            pressure = domain.pressure,
            humidity = domain.humidity,
            windSpeed = domain.windSpeed,
            windDirection = domain.windDirection,
            cloudiness = domain.cloudiness,
            precipitation = domain.precipitation,
            moonPhase = domain.moonPhase
        )
    }
}
