package com.example.trophy.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.trophy.domain.model.LocationType
import java.time.LocalDateTime

/**
 * Room Entity для места/локации.
 */
@Entity(tableName = "locations")
data class LocationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val name: String,

    val description: String? = null,

    @ColumnInfo(name = "location_type")
    val locationType: LocationType,

    val latitude: Double,

    val longitude: Double,

    @ColumnInfo(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now()
)
