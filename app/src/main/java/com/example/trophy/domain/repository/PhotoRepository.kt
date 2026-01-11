package com.example.trophy.domain.repository

import com.example.trophy.domain.model.Photo
import kotlinx.coroutines.flow.Flow

/**
 * Интерфейс репозитория для работы с фотографиями.
 */
interface PhotoRepository {

    fun getPhotosByCatchId(catchId: Long): Flow<List<Photo>>

    fun getAllPhotos(): Flow<List<Photo>>

    suspend fun getPhotoById(id: Long): Photo?

    suspend fun insertPhoto(photo: Photo): Long

    suspend fun updatePhoto(photo: Photo)

    suspend fun deletePhoto(photo: Photo)

    suspend fun deletePhotoById(id: Long)

    suspend fun setPrimaryPhoto(catchId: Long, photoId: Long)
}
