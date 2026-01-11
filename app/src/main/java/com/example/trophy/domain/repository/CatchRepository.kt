package com.example.trophy.domain.repository

import com.example.trophy.domain.model.ActivityType
import com.example.trophy.domain.model.Catch
import kotlinx.coroutines.flow.Flow

/**
 * Интерфейс репозитория для работы с уловами/трофеями.
 */
interface CatchRepository {

    fun getCatches(): Flow<List<Catch>>

    fun getCatchesByType(type: ActivityType): Flow<List<Catch>>

    fun getCatchesByLocationId(locationId: Long): Flow<List<Catch>>

    suspend fun getCatchById(id: Long): Catch?

    suspend fun insertCatch(catch: Catch): Long

    suspend fun updateCatch(catch: Catch)

    suspend fun deleteCatch(catch: Catch)

    suspend fun deleteCatchById(id: Long)
}
