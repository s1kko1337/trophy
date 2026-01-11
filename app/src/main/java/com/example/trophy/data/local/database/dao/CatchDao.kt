package com.example.trophy.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.trophy.data.local.database.entity.CatchEntity
import com.example.trophy.domain.model.ActivityType
import kotlinx.coroutines.flow.Flow

/**
 * DAO для работы с уловами/трофеями.
 */
@Dao
interface CatchDao {

    @Query("SELECT * FROM catches ORDER BY catch_date DESC, catch_time DESC")
    fun getAllCatches(): Flow<List<CatchEntity>>

    @Query("SELECT * FROM catches WHERE activity_type = :type ORDER BY catch_date DESC, catch_time DESC")
    fun getCatchesByType(type: ActivityType): Flow<List<CatchEntity>>

    @Query("SELECT * FROM catches WHERE location_id = :locationId ORDER BY catch_date DESC")
    fun getCatchesByLocationId(locationId: Long): Flow<List<CatchEntity>>

    @Query("SELECT * FROM catches WHERE id = :id")
    suspend fun getCatchById(id: Long): CatchEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(catchEntity: CatchEntity): Long

    @Update
    suspend fun update(catchEntity: CatchEntity)

    @Delete
    suspend fun delete(catchEntity: CatchEntity)

    @Query("DELETE FROM catches WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT COUNT(*) FROM catches")
    suspend fun getCatchCount(): Int

    @Query("SELECT COUNT(*) FROM catches WHERE activity_type = :type")
    suspend fun getCatchCountByType(type: ActivityType): Int

    @Query("SELECT SUM(weight) FROM catches WHERE weight IS NOT NULL")
    suspend fun getTotalWeight(): Double?

    @Query("SELECT * FROM catches WHERE species LIKE '%' || :query || '%' ORDER BY catch_date DESC")
    fun searchBySpecies(query: String): Flow<List<CatchEntity>>
}
