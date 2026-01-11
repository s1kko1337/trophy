package com.example.trophy.data.mapper

import com.example.trophy.data.local.database.entity.CatchEntity
import com.example.trophy.domain.model.Catch
import javax.inject.Inject

/**
 * Маппер для конвертации между CatchEntity и Catch domain model.
 */
class CatchMapper @Inject constructor() {

    fun toDomain(entity: CatchEntity): Catch {
        return Catch(
            id = entity.id,
            activityType = entity.activityType,
            species = entity.species,
            weight = entity.weight,
            length = entity.length,
            quantity = entity.quantity,
            locationId = entity.locationId,
            weatherId = entity.weatherId,
            notes = entity.notes,
            catchDate = entity.catchDate,
            catchTime = entity.catchTime,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
    }

    fun toEntity(domain: Catch): CatchEntity {
        return CatchEntity(
            id = domain.id,
            activityType = domain.activityType,
            species = domain.species,
            weight = domain.weight,
            length = domain.length,
            quantity = domain.quantity,
            locationId = domain.locationId,
            weatherId = domain.weatherId,
            notes = domain.notes,
            catchDate = domain.catchDate,
            catchTime = domain.catchTime,
            createdAt = domain.createdAt,
            updatedAt = domain.updatedAt
        )
    }

    fun toDomainList(entities: List<CatchEntity>): List<Catch> {
        return entities.map { toDomain(it) }
    }
}
