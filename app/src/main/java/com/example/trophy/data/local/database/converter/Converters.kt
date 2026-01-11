package com.example.trophy.data.local.database.converter

import androidx.room.TypeConverter
import com.example.trophy.domain.model.ActivityType
import com.example.trophy.domain.model.Cloudiness
import com.example.trophy.domain.model.EquipmentType
import com.example.trophy.domain.model.LocationType
import com.example.trophy.domain.model.MoonPhase
import com.example.trophy.domain.model.Precipitation
import com.example.trophy.domain.model.WindDirection
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

/**
 * Room Type Converters для конвертации типов данных.
 */
class Converters {

    // LocalDateTime
    @TypeConverter
    fun fromLocalDateTime(value: LocalDateTime?): String? {
        return value?.toString()
    }

    @TypeConverter
    fun toLocalDateTime(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it) }
    }

    // LocalDate
    @TypeConverter
    fun fromLocalDate(value: LocalDate?): String? {
        return value?.toString()
    }

    @TypeConverter
    fun toLocalDate(value: String?): LocalDate? {
        return value?.let { LocalDate.parse(it) }
    }

    // LocalTime
    @TypeConverter
    fun fromLocalTime(value: LocalTime?): String? {
        return value?.toString()
    }

    @TypeConverter
    fun toLocalTime(value: String?): LocalTime? {
        return value?.let { LocalTime.parse(it) }
    }

    // ActivityType
    @TypeConverter
    fun fromActivityType(value: ActivityType?): String? {
        return value?.name
    }

    @TypeConverter
    fun toActivityType(value: String?): ActivityType? {
        return value?.let { ActivityType.valueOf(it) }
    }

    // LocationType
    @TypeConverter
    fun fromLocationType(value: LocationType?): String? {
        return value?.name
    }

    @TypeConverter
    fun toLocationType(value: String?): LocationType? {
        return value?.let { LocationType.valueOf(it) }
    }

    // EquipmentType
    @TypeConverter
    fun fromEquipmentType(value: EquipmentType?): String? {
        return value?.name
    }

    @TypeConverter
    fun toEquipmentType(value: String?): EquipmentType? {
        return value?.let { EquipmentType.valueOf(it) }
    }

    // WindDirection
    @TypeConverter
    fun fromWindDirection(value: WindDirection?): String? {
        return value?.name
    }

    @TypeConverter
    fun toWindDirection(value: String?): WindDirection? {
        return value?.let { WindDirection.valueOf(it) }
    }

    // Cloudiness
    @TypeConverter
    fun fromCloudiness(value: Cloudiness?): String? {
        return value?.name
    }

    @TypeConverter
    fun toCloudiness(value: String?): Cloudiness? {
        return value?.let { Cloudiness.valueOf(it) }
    }

    // Precipitation
    @TypeConverter
    fun fromPrecipitation(value: Precipitation?): String? {
        return value?.name
    }

    @TypeConverter
    fun toPrecipitation(value: String?): Precipitation? {
        return value?.let { Precipitation.valueOf(it) }
    }

    // MoonPhase
    @TypeConverter
    fun fromMoonPhase(value: MoonPhase?): String? {
        return value?.name
    }

    @TypeConverter
    fun toMoonPhase(value: String?): MoonPhase? {
        return value?.let { MoonPhase.valueOf(it) }
    }
}
