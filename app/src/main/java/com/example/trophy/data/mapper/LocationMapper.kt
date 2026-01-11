package com.example.trophy.data.mapper

import com.example.trophy.data.local.database.entity.LocationEntity
import com.example.trophy.domain.model.Location
import javax.inject.Inject

/**
 * Маппер для конвертации между LocationEntity и Location domain model.
 */
class LocationMapper @Inject constructor() {

    fun toDomain(entity: LocationEntity): Location {
        return Location(
            id = entity.id,
            name = entity.name,
            description = entity.description,
            locationType = entity.locationType,
            latitude = entity.latitude,
            longitude = entity.longitude,
            createdAt = entity.createdAt
        )
    }

    fun toEntity(domain: Location): LocationEntity {
        return LocationEntity(
            id = domain.id,
            name = domain.name,
            description = domain.description,
            locationType = domain.locationType,
            latitude = domain.latitude,
            longitude = domain.longitude,
            createdAt = domain.createdAt
        )
    }

    fun toDomainList(entities: List<LocationEntity>): List<Location> {
        return entities.map { toDomain(it) }
    }
}
