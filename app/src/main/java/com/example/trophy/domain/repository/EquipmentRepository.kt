package com.example.trophy.domain.repository

import com.example.trophy.domain.model.ActivityType
import com.example.trophy.domain.model.Equipment
import kotlinx.coroutines.flow.Flow

/**
 * Интерфейс репозитория для работы со снаряжением.
 */
interface EquipmentRepository {

    fun getEquipment(): Flow<List<Equipment>>

    fun getEquipmentByActivityType(activityType: ActivityType): Flow<List<Equipment>>

    fun getEquipmentByCatchId(catchId: Long): Flow<List<Equipment>>

    suspend fun getEquipmentById(id: Long): Equipment?

    suspend fun insertEquipment(equipment: Equipment): Long

    suspend fun updateEquipment(equipment: Equipment)

    suspend fun deleteEquipment(equipment: Equipment)

    suspend fun deleteEquipmentById(id: Long)

    suspend fun addEquipmentToCatch(catchId: Long, equipmentId: Long)

    suspend fun removeEquipmentFromCatch(catchId: Long, equipmentId: Long)

    suspend fun clearEquipmentFromCatch(catchId: Long)
}
