package com.example.trophy.data.repository

import com.example.trophy.data.local.database.dao.CatchDao
import com.example.trophy.data.mapper.CatchMapper
import com.example.trophy.domain.model.ActivityType
import com.example.trophy.domain.model.Catch
import com.example.trophy.domain.repository.CatchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Реализация репозитория для работы с уловами/трофеями.
 */
class CatchRepositoryImpl @Inject constructor(
    private val catchDao: CatchDao,
    private val mapper: CatchMapper
) : CatchRepository {

    override fun getCatches(): Flow<List<Catch>> {
        return catchDao.getAllCatches().map { entities ->
            mapper.toDomainList(entities)
        }
    }

    override fun getCatchesByType(type: ActivityType): Flow<List<Catch>> {
        return catchDao.getCatchesByType(type).map { entities ->
            mapper.toDomainList(entities)
        }
    }

    override fun getCatchesByLocationId(locationId: Long): Flow<List<Catch>> {
        return catchDao.getCatchesByLocationId(locationId).map { entities ->
            mapper.toDomainList(entities)
        }
    }

    override suspend fun getCatchById(id: Long): Catch? {
        return catchDao.getCatchById(id)?.let { mapper.toDomain(it) }
    }

    override suspend fun insertCatch(catch: Catch): Long {
        return catchDao.insert(mapper.toEntity(catch))
    }

    override suspend fun updateCatch(catch: Catch) {
        catchDao.update(mapper.toEntity(catch))
    }

    override suspend fun deleteCatch(catch: Catch) {
        catchDao.delete(mapper.toEntity(catch))
    }

    override suspend fun deleteCatchById(id: Long) {
        catchDao.deleteById(id)
    }

    override suspend fun isDuplicate(catch: Catch): Boolean {
        return catchDao.isDuplicate(
            species = catch.species,
            date = catch.catchDate.toString(),
            activityType = catch.activityType,
            locationId = catch.locationId
        )
    }
}
