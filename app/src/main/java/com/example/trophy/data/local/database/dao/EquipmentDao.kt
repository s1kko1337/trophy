package com.example.trophy.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.trophy.data.local.database.entity.CatchEquipmentCrossRef
import com.example.trophy.data.local.database.entity.EquipmentEntity
import com.example.trophy.domain.model.ActivityType
import kotlinx.coroutines.flow.Flow

/**
 * DAO для работы со снаряжением.
 */
@Dao
interface EquipmentDao {

    @Query("SELECT * FROM equipment ORDER BY name ASC")
    fun getAllEquipment(): Flow<List<EquipmentEntity>>

    @Query("SELECT * FROM equipment WHERE activity_type = :activityType ORDER BY name ASC")
    fun getEquipmentByActivityType(activityType: ActivityType): Flow<List<EquipmentEntity>>

    @Query("""
        SELECT e.* FROM equipment e
        INNER JOIN catch_equipment ce ON e.id = ce.equipment_id
        WHERE ce.catch_id = :catchId
        ORDER BY e.name ASC
    """)
    fun getEquipmentByCatchId(catchId: Long): Flow<List<EquipmentEntity>>

    @Query("SELECT * FROM equipment WHERE id = :id")
    suspend fun getEquipmentById(id: Long): EquipmentEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(equipment: EquipmentEntity): Long

    @Update
    suspend fun update(equipment: EquipmentEntity)

    @Delete
    suspend fun delete(equipment: EquipmentEntity)

    @Query("DELETE FROM equipment WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCatchEquipmentCrossRef(crossRef: CatchEquipmentCrossRef)

    @Query("DELETE FROM catch_equipment WHERE catch_id = :catchId AND equipment_id = :equipmentId")
    suspend fun deleteCatchEquipmentCrossRef(catchId: Long, equipmentId: Long)

    @Query("DELETE FROM catch_equipment WHERE catch_id = :catchId")
    suspend fun deleteAllCatchEquipmentCrossRefs(catchId: Long)
}
