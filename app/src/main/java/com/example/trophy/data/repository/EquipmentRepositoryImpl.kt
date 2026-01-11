package com.example.trophy.data.repository

import com.example.trophy.data.local.database.dao.EquipmentDao
import com.example.trophy.data.local.database.entity.CatchEquipmentCrossRef
import com.example.trophy.data.mapper.EquipmentMapper
import com.example.trophy.domain.model.ActivityType
import com.example.trophy.domain.model.Equipment
import com.example.trophy.domain.repository.EquipmentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Реализация репозитория для работы со снаряжением.
 */
class EquipmentRepositoryImpl @Inject constructor(
    private val equipmentDao: EquipmentDao,
    private val mapper: EquipmentMapper
) : EquipmentRepository {

    override fun getEquipment(): Flow<List<Equipment>> {
        return equipmentDao.getAllEquipment().map { entities ->
            mapper.toDomainList(entities)
        }
    }

    override fun getEquipmentByActivityType(activityType: ActivityType): Flow<List<Equipment>> {
        return equipmentDao.getEquipmentByActivityType(activityType).map { entities ->
            mapper.toDomainList(entities)
        }
    }

    override fun getEquipmentByCatchId(catchId: Long): Flow<List<Equipment>> {
        return equipmentDao.getEquipmentByCatchId(catchId).map { entities ->
            mapper.toDomainList(entities)
        }
    }

    override suspend fun getEquipmentById(id: Long): Equipment? {
        return equipmentDao.getEquipmentById(id)?.let { mapper.toDomain(it) }
    }

    override suspend fun insertEquipment(equipment: Equipment): Long {
        return equipmentDao.insert(mapper.toEntity(equipment))
    }

    override suspend fun updateEquipment(equipment: Equipment) {
        equipmentDao.update(mapper.toEntity(equipment))
    }

    override suspend fun deleteEquipment(equipment: Equipment) {
        equipmentDao.delete(mapper.toEntity(equipment))
    }

    override suspend fun deleteEquipmentById(id: Long) {
        equipmentDao.deleteById(id)
    }

    override suspend fun addEquipmentToCatch(catchId: Long, equipmentId: Long) {
        equipmentDao.insertCatchEquipmentCrossRef(
            CatchEquipmentCrossRef(catchId = catchId, equipmentId = equipmentId)
        )
    }

    override suspend fun removeEquipmentFromCatch(catchId: Long, equipmentId: Long) {
        equipmentDao.deleteCatchEquipmentCrossRef(catchId, equipmentId)
    }

    override suspend fun clearEquipmentFromCatch(catchId: Long) {
        equipmentDao.deleteAllCatchEquipmentCrossRefs(catchId)
    }
}
