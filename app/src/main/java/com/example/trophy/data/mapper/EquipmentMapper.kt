package com.example.trophy.data.mapper

import com.example.trophy.data.local.database.entity.EquipmentEntity
import com.example.trophy.domain.model.Equipment
import javax.inject.Inject

/**
 * Маппер для конвертации между EquipmentEntity и Equipment domain model.
 */
class EquipmentMapper @Inject constructor() {

    fun toDomain(entity: EquipmentEntity): Equipment {
        return Equipment(
            id = entity.id,
            name = entity.name,
            description = entity.description,
            equipmentType = entity.equipmentType,
            activityType = entity.activityType,
            createdAt = entity.createdAt
        )
    }

    fun toEntity(domain: Equipment): EquipmentEntity {
        return EquipmentEntity(
            id = domain.id,
            name = domain.name,
            description = domain.description,
            equipmentType = domain.equipmentType,
            activityType = domain.activityType,
            createdAt = domain.createdAt
        )
    }

    fun toDomainList(entities: List<EquipmentEntity>): List<Equipment> {
        return entities.map { toDomain(it) }
    }
}
