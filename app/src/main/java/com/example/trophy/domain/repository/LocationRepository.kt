package com.example.trophy.domain.repository

import com.example.trophy.domain.model.Location
import kotlinx.coroutines.flow.Flow

/**
 * Интерфейс репозитория для работы с местами.
 */
interface LocationRepository {

    fun getLocations(): Flow<List<Location>>

    suspend fun getLocationById(id: Long): Location?

    suspend fun insertLocation(location: Location): Long

    suspend fun updateLocation(location: Location)

    suspend fun deleteLocation(location: Location)

    suspend fun deleteLocationById(id: Long)
}
