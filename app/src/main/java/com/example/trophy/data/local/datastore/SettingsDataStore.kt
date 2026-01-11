package com.example.trophy.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.trophy.domain.model.ActivityType
import com.example.trophy.domain.repository.SortOption
import com.example.trophy.domain.repository.ThemeMode
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

/**
 * DataStore для хранения настроек приложения.
 */
@Singleton
class SettingsDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object PreferencesKeys {
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val DYNAMIC_COLOR_ENABLED = booleanPreferencesKey("dynamic_color_enabled")
        val DEFAULT_SORT_OPTION = stringPreferencesKey("default_sort_option")
        val PHOTO_QUALITY = intPreferencesKey("photo_quality")
        val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
        val LAST_ACTIVITY_TYPE = stringPreferencesKey("last_activity_type")
    }

    val themeMode: Flow<ThemeMode> = context.dataStore.data.map { preferences ->
        val themeName = preferences[PreferencesKeys.THEME_MODE] ?: ThemeMode.SYSTEM.name
        try {
            ThemeMode.valueOf(themeName)
        } catch (e: IllegalArgumentException) {
            ThemeMode.SYSTEM
        }
    }

    val dynamicColorEnabled: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.DYNAMIC_COLOR_ENABLED] ?: true
    }

    val defaultSortOption: Flow<SortOption> = context.dataStore.data.map { preferences ->
        val sortName = preferences[PreferencesKeys.DEFAULT_SORT_OPTION] ?: SortOption.DATE_DESC.name
        try {
            SortOption.valueOf(sortName)
        } catch (e: IllegalArgumentException) {
            SortOption.DATE_DESC
        }
    }

    val photoQuality: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.PHOTO_QUALITY] ?: 80
    }

    val onboardingCompleted: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.ONBOARDING_COMPLETED] ?: false
    }

    val lastActivityType: Flow<ActivityType> = context.dataStore.data.map { preferences ->
        val activityName = preferences[PreferencesKeys.LAST_ACTIVITY_TYPE] ?: ActivityType.FISHING.name
        try {
            ActivityType.valueOf(activityName)
        } catch (e: IllegalArgumentException) {
            ActivityType.FISHING
        }
    }

    suspend fun setThemeMode(themeMode: ThemeMode) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.THEME_MODE] = themeMode.name
        }
    }

    suspend fun setDynamicColorEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.DYNAMIC_COLOR_ENABLED] = enabled
        }
    }

    suspend fun setDefaultSortOption(sortOption: SortOption) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.DEFAULT_SORT_OPTION] = sortOption.name
        }
    }

    suspend fun setPhotoQuality(quality: Int) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.PHOTO_QUALITY] = quality.coerceIn(10, 100)
        }
    }

    suspend fun setOnboardingCompleted(completed: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.ONBOARDING_COMPLETED] = completed
        }
    }

    suspend fun setLastActivityType(activityType: ActivityType) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.LAST_ACTIVITY_TYPE] = activityType.name
        }
    }
}
