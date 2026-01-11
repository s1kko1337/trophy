package com.example.trophy.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.trophy.data.local.database.converter.Converters
import com.example.trophy.data.local.database.dao.CatchDao
import com.example.trophy.data.local.database.dao.EquipmentDao
import com.example.trophy.data.local.database.dao.LocationDao
import com.example.trophy.data.local.database.dao.PhotoDao
import com.example.trophy.data.local.database.dao.WeatherDao
import com.example.trophy.data.local.database.entity.CatchEntity
import com.example.trophy.data.local.database.entity.CatchEquipmentCrossRef
import com.example.trophy.data.local.database.entity.EquipmentEntity
import com.example.trophy.data.local.database.entity.LocationEntity
import com.example.trophy.data.local.database.entity.PhotoEntity
import com.example.trophy.data.local.database.entity.WeatherEntity

/**
 * Основная база данных приложения Room.
 */
@Database(
    entities = [
        CatchEntity::class,
        LocationEntity::class,
        PhotoEntity::class,
        WeatherEntity::class,
        EquipmentEntity::class,
        CatchEquipmentCrossRef::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun catchDao(): CatchDao
    abstract fun locationDao(): LocationDao
    abstract fun photoDao(): PhotoDao
    abstract fun weatherDao(): WeatherDao
    abstract fun equipmentDao(): EquipmentDao
}
