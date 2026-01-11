package com.example.trophy.domain.model

import java.time.LocalDateTime

/**
 * Domain модель фотографии.
 */
data class Photo(
    val id: Long = 0,
    val catchId: Long,
    val filePath: String,
    val isPrimary: Boolean = false,
    val createdAt: LocalDateTime = LocalDateTime.now()
)
