package com.example.trophy.domain.model

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

/**
 * Domain модель улова/трофея.
 */
data class Catch(
    val id: Long = 0,
    val activityType: ActivityType,
    val species: String,
    val weight: Double? = null,
    val length: Double? = null,
    val quantity: Int = 1,
    val locationId: Long? = null,
    val weatherId: Long? = null,
    val notes: String? = null,
    val catchDate: LocalDate,
    val catchTime: LocalTime? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)
