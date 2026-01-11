package com.example.trophy.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.trophy.data.local.database.entity.WeatherEntity

/**
 * DAO для работы с погодными условиями.
 */
@Dao
interface WeatherDao {

    @Query("SELECT * FROM weather WHERE id = :id")
    suspend fun getWeatherById(id: Long): WeatherEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(weather: WeatherEntity): Long

    @Update
    suspend fun update(weather: WeatherEntity)

    @Delete
    suspend fun delete(weather: WeatherEntity)

    @Query("DELETE FROM weather WHERE id = :id")
    suspend fun deleteById(id: Long)
}
