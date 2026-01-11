package com.example.trophy.di

import com.example.trophy.data.repository.CatchRepositoryImpl
import com.example.trophy.data.repository.EquipmentRepositoryImpl
import com.example.trophy.data.repository.LocationRepositoryImpl
import com.example.trophy.data.repository.PhotoRepositoryImpl
import com.example.trophy.data.repository.SettingsRepositoryImpl
import com.example.trophy.data.repository.WeatherRepositoryImpl
import com.example.trophy.domain.repository.CatchRepository
import com.example.trophy.domain.repository.EquipmentRepository
import com.example.trophy.domain.repository.LocationRepository
import com.example.trophy.domain.repository.PhotoRepository
import com.example.trophy.domain.repository.SettingsRepository
import com.example.trophy.domain.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt модуль для связывания интерфейсов репозиториев с их реализациями.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCatchRepository(
        impl: CatchRepositoryImpl
    ): CatchRepository

    @Binds
    @Singleton
    abstract fun bindLocationRepository(
        impl: LocationRepositoryImpl
    ): LocationRepository

    @Binds
    @Singleton
    abstract fun bindPhotoRepository(
        impl: PhotoRepositoryImpl
    ): PhotoRepository

    @Binds
    @Singleton
    abstract fun bindWeatherRepository(
        impl: WeatherRepositoryImpl
    ): WeatherRepository

    @Binds
    @Singleton
    abstract fun bindEquipmentRepository(
        impl: EquipmentRepositoryImpl
    ): EquipmentRepository

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(
        impl: SettingsRepositoryImpl
    ): SettingsRepository
}
