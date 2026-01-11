package com.example.trophy.domain.model

import java.time.LocalDateTime

/**
 * Domain модель снаряжения.
 */
data class Equipment(
    val id: Long = 0,
    val name: String,
    val description: String? = null,
    val equipmentType: EquipmentType,
    val activityType: ActivityType,
    val createdAt: LocalDateTime = LocalDateTime.now()
)
