package com.example.trophy.data.repository

import com.example.trophy.data.local.datastore.SettingsDataStore
import com.example.trophy.domain.model.ActivityType
import com.example.trophy.domain.repository.SettingsRepository
import com.example.trophy.domain.repository.SortOption
import com.example.trophy.domain.repository.ThemeMode
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Реализация репозитория для работы с настройками приложения.
 */
class SettingsRepositoryImpl @Inject constructor(
    private val settingsDataStore: SettingsDataStore
) : SettingsRepository {

    override val themeMode: Flow<ThemeMode> = settingsDataStore.themeMode

    override val dynamicColorEnabled: Flow<Boolean> = settingsDataStore.dynamicColorEnabled

    override val defaultSortOption: Flow<SortOption> = settingsDataStore.defaultSortOption

    override val photoQuality: Flow<Int> = settingsDataStore.photoQuality

    override val onboardingCompleted: Flow<Boolean> = settingsDataStore.onboardingCompleted

    override val lastActivityType: Flow<ActivityType> = settingsDataStore.lastActivityType

    override suspend fun setThemeMode(themeMode: ThemeMode) {
        settingsDataStore.setThemeMode(themeMode)
    }

    override suspend fun setDynamicColorEnabled(enabled: Boolean) {
        settingsDataStore.setDynamicColorEnabled(enabled)
    }

    override suspend fun setDefaultSortOption(sortOption: SortOption) {
        settingsDataStore.setDefaultSortOption(sortOption)
    }

    override suspend fun setPhotoQuality(quality: Int) {
        settingsDataStore.setPhotoQuality(quality)
    }

    override suspend fun setOnboardingCompleted(completed: Boolean) {
        settingsDataStore.setOnboardingCompleted(completed)
    }

    override suspend fun setLastActivityType(activityType: ActivityType) {
        settingsDataStore.setLastActivityType(activityType)
    }
}
