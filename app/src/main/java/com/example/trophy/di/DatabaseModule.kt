package com.example.trophy.di

import android.content.Context
import androidx.room.Room
import com.example.trophy.data.local.database.AppDatabase
import com.example.trophy.data.local.database.dao.CatchDao
import com.example.trophy.data.local.database.dao.EquipmentDao
import com.example.trophy.data.local.database.dao.LocationDao
import com.example.trophy.data.local.database.dao.PhotoDao
import com.example.trophy.data.local.database.dao.WeatherDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt модуль для Room Database.
 * Предоставляет экземпляр базы данных и всех DAO.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    private const val DATABASE_NAME = "trophy_database"

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideCatchDao(database: AppDatabase): CatchDao {
        return database.catchDao()
    }

    @Provides
    @Singleton
    fun provideLocationDao(database: AppDatabase): LocationDao {
        return database.locationDao()
    }

    @Provides
    @Singleton
    fun providePhotoDao(database: AppDatabase): PhotoDao {
        return database.photoDao()
    }

    @Provides
    @Singleton
    fun provideWeatherDao(database: AppDatabase): WeatherDao {
        return database.weatherDao()
    }

    @Provides
    @Singleton
    fun provideEquipmentDao(database: AppDatabase): EquipmentDao {
        return database.equipmentDao()
    }
}
