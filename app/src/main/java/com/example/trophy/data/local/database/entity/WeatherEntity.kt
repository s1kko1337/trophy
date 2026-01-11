package com.example.trophy.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.trophy.domain.model.Cloudiness
import com.example.trophy.domain.model.MoonPhase
import com.example.trophy.domain.model.Precipitation
import com.example.trophy.domain.model.WindDirection

/**
 * Room Entity для погодных условий.
 */
@Entity(tableName = "weather")
data class WeatherEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val temperature: Int? = null,

    val pressure: Int? = null,

    val humidity: Int? = null,

    @ColumnInfo(name = "wind_speed")
    val windSpeed: Double? = null,

    @ColumnInfo(name = "wind_direction")
    val windDirection: WindDirection? = null,

    val cloudiness: Cloudiness? = null,

    val precipitation: Precipitation? = null,

    @ColumnInfo(name = "moon_phase")
    val moonPhase: MoonPhase? = null
)
