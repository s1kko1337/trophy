package com.example.trophy.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

/**
 * Кросс-таблица для связи many-to-many между уловами и снаряжением.
 */
@Entity(
    tableName = "catch_equipment",
    primaryKeys = ["catch_id", "equipment_id"],
    foreignKeys = [
        ForeignKey(
            entity = CatchEntity::class,
            parentColumns = ["id"],
            childColumns = ["catch_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = EquipmentEntity::class,
            parentColumns = ["id"],
            childColumns = ["equipment_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class CatchEquipmentCrossRef(
    @ColumnInfo(name = "catch_id", index = true)
    val catchId: Long,

    @ColumnInfo(name = "equipment_id", index = true)
    val equipmentId: Long
)
