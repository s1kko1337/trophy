package com.example.trophy.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.trophy.data.local.database.entity.PhotoEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO для работы с фотографиями.
 */
@Dao
interface PhotoDao {

    @Query("SELECT * FROM photos WHERE catch_id = :catchId ORDER BY is_primary DESC, created_at DESC")
    fun getPhotosByCatchId(catchId: Long): Flow<List<PhotoEntity>>

    @Query("SELECT * FROM photos ORDER BY created_at DESC")
    fun getAllPhotos(): Flow<List<PhotoEntity>>

    @Query("SELECT * FROM photos WHERE id = :id")
    suspend fun getPhotoById(id: Long): PhotoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(photo: PhotoEntity): Long

    @Update
    suspend fun update(photo: PhotoEntity)

    @Delete
    suspend fun delete(photo: PhotoEntity)

    @Query("DELETE FROM photos WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("UPDATE photos SET is_primary = 0 WHERE catch_id = :catchId")
    suspend fun clearPrimaryPhotos(catchId: Long)

    @Query("UPDATE photos SET is_primary = 1 WHERE id = :photoId")
    suspend fun setPrimaryPhoto(photoId: Long)

    @Query("SELECT * FROM photos WHERE catch_id = :catchId AND is_primary = 1 LIMIT 1")
    suspend fun getPrimaryPhoto(catchId: Long): PhotoEntity?
}
