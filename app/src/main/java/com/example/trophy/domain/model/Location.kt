package com.example.trophy.domain.model

import java.time.LocalDateTime

/**
 * Domain модель местоположения/места.
 */
data class Location(
    val id: Long = 0,
    val name: String,
    val description: String? = null,
    val locationType: LocationType,
    val latitude: Double,
    val longitude: Double,
    val createdAt: LocalDateTime = LocalDateTime.now()
)
