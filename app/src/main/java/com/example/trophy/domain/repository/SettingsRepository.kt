package com.example.trophy.domain.repository

import com.example.trophy.domain.model.ActivityType
import kotlinx.coroutines.flow.Flow

/**
 * Настройки темы.
 */
enum class ThemeMode {
    LIGHT,
    DARK,
    SYSTEM
}

/**
 * Настройки сортировки.
 */
enum class SortOption {
    DATE_DESC,
    DATE_ASC,
    WEIGHT_DESC,
    WEIGHT_ASC,
    SPECIES
}

/**
 * Интерфейс репозитория для работы с настройками приложения.
 */
interface SettingsRepository {

    val themeMode: Flow<ThemeMode>

    val dynamicColorEnabled: Flow<Boolean>

    val defaultSortOption: Flow<SortOption>

    val photoQuality: Flow<Int>

    val onboardingCompleted: Flow<Boolean>

    val lastActivityType: Flow<ActivityType>

    suspend fun setThemeMode(themeMode: ThemeMode)

    suspend fun setDynamicColorEnabled(enabled: Boolean)

    suspend fun setDefaultSortOption(sortOption: SortOption)

    suspend fun setPhotoQuality(quality: Int)

    suspend fun setOnboardingCompleted(completed: Boolean)

    suspend fun setLastActivityType(activityType: ActivityType)
}
