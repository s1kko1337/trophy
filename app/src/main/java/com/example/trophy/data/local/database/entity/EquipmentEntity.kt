package com.example.trophy.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.trophy.domain.model.ActivityType
import com.example.trophy.domain.model.EquipmentType
import java.time.LocalDateTime

/**
 * Room Entity для снаряжения.
 */
@Entity(tableName = "equipment")
data class EquipmentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val name: String,

    val description: String? = null,

    @ColumnInfo(name = "equipment_type")
    val equipmentType: EquipmentType,

    @ColumnInfo(name = "activity_type")
    val activityType: ActivityType,

    @ColumnInfo(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now()
)
