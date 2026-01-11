package com.example.trophy.data.mapper

import com.example.trophy.data.local.database.entity.PhotoEntity
import com.example.trophy.domain.model.Photo
import javax.inject.Inject

/**
 * Маппер для конвертации между PhotoEntity и Photo domain model.
 */
class PhotoMapper @Inject constructor() {

    fun toDomain(entity: PhotoEntity): Photo {
        return Photo(
            id = entity.id,
            catchId = entity.catchId,
            filePath = entity.filePath,
            isPrimary = entity.isPrimary,
            createdAt = entity.createdAt
        )
    }

    fun toEntity(domain: Photo): PhotoEntity {
        return PhotoEntity(
            id = domain.id,
            catchId = domain.catchId,
            filePath = domain.filePath,
            isPrimary = domain.isPrimary,
            createdAt = domain.createdAt
        )
    }

    fun toDomainList(entities: List<PhotoEntity>): List<Photo> {
        return entities.map { toDomain(it) }
    }
}
