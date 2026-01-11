package com.example.trophy.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.trophy.domain.model.ActivityType
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

/**
 * Room Entity для улова/трофея.
 */
@Entity(
    tableName = "catches",
    foreignKeys = [
        ForeignKey(
            entity = LocationEntity::class,
            parentColumns = ["id"],
            childColumns = ["location_id"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = WeatherEntity::class,
            parentColumns = ["id"],
            childColumns = ["weather_id"],
            onDelete = ForeignKey.SET_NULL
        )
    ]
)
data class CatchEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "activity_type")
    val activityType: ActivityType,

    val species: String,

    val weight: Double? = null,

    val length: Double? = null,

    val quantity: Int = 1,

    @ColumnInfo(name = "location_id", index = true)
    val locationId: Long? = null,

    @ColumnInfo(name = "weather_id", index = true)
    val weatherId: Long? = null,

    val notes: String? = null,

    @ColumnInfo(name = "catch_date")
    val catchDate: LocalDate,

    @ColumnInfo(name = "catch_time")
    val catchTime: LocalTime? = null,

    @ColumnInfo(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @ColumnInfo(name = "updated_at")
    val updatedAt: LocalDateTime = LocalDateTime.now()
)
