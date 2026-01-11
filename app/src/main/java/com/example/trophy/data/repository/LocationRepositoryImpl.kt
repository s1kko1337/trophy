package com.example.trophy.data.repository

import com.example.trophy.data.local.database.dao.LocationDao
import com.example.trophy.data.mapper.LocationMapper
import com.example.trophy.domain.model.Location
import com.example.trophy.domain.repository.LocationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Реализация репозитория для работы с местами.
 */
class LocationRepositoryImpl @Inject constructor(
    private val locationDao: LocationDao,
    private val mapper: LocationMapper
) : LocationRepository {

    override fun getLocations(): Flow<List<Location>> {
        return locationDao.getAllLocations().map { entities ->
            mapper.toDomainList(entities)
        }
    }

    override suspend fun getLocationById(id: Long): Location? {
        return locationDao.getLocationById(id)?.let { mapper.toDomain(it) }
    }

    override suspend fun insertLocation(location: Location): Long {
        return locationDao.insert(mapper.toEntity(location))
    }

    override suspend fun updateLocation(location: Location) {
        locationDao.update(mapper.toEntity(location))
    }

    override suspend fun deleteLocation(location: Location) {
        locationDao.delete(mapper.toEntity(location))
    }

    override suspend fun deleteLocationById(id: Long) {
        locationDao.deleteById(id)
    }
}
