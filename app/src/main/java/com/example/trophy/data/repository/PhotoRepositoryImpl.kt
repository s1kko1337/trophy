package com.example.trophy.data.repository

import com.example.trophy.data.local.database.dao.PhotoDao
import com.example.trophy.data.mapper.PhotoMapper
import com.example.trophy.domain.model.Photo
import com.example.trophy.domain.repository.PhotoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Реализация репозитория для работы с фотографиями.
 */
class PhotoRepositoryImpl @Inject constructor(
    private val photoDao: PhotoDao,
    private val mapper: PhotoMapper
) : PhotoRepository {

    override fun getPhotosByCatchId(catchId: Long): Flow<List<Photo>> {
        return photoDao.getPhotosByCatchId(catchId).map { entities ->
            mapper.toDomainList(entities)
        }
    }

    override fun getAllPhotos(): Flow<List<Photo>> {
        return photoDao.getAllPhotos().map { entities ->
            mapper.toDomainList(entities)
        }
    }

    override suspend fun getPhotoById(id: Long): Photo? {
        return photoDao.getPhotoById(id)?.let { mapper.toDomain(it) }
    }

    override suspend fun insertPhoto(photo: Photo): Long {
        return photoDao.insert(mapper.toEntity(photo))
    }

    override suspend fun updatePhoto(photo: Photo) {
        photoDao.update(mapper.toEntity(photo))
    }

    override suspend fun deletePhoto(photo: Photo) {
        photoDao.delete(mapper.toEntity(photo))
    }

    override suspend fun deletePhotoById(id: Long) {
        photoDao.deleteById(id)
    }

    override suspend fun setPrimaryPhoto(catchId: Long, photoId: Long) {
        photoDao.clearPrimaryPhotos(catchId)
        photoDao.setPrimaryPhoto(photoId)
    }
}
