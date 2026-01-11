package com.example.trophy.presentation.screens.statistics

import com.example.trophy.domain.model.ActivityType

/**
 * UI State для экрана статистики.
 */
data class StatisticsUiState(
    val selectedTab: ActivityType = ActivityType.FISHING,
    val isLoading: Boolean = true,
    val error: String? = null,

    // Общая статистика
    val totalCatches: Int = 0,
    val totalWeight: Double = 0.0,
    val avgWeight: Double = 0.0,
    val bestCatchWeight: Double = 0.0,
    val bestCatchSpecies: String? = null,

    // По видам
    val speciesStats: List<SpeciesStat> = emptyList(),

    // По месяцам
    val monthlyStats: List<MonthlyStat> = emptyList(),

    // По местам
    val locationStats: List<LocationStat> = emptyList()
)

/**
 * Статистика по виду рыбы/дичи.
 */
data class SpeciesStat(
    val species: String,
    val count: Int,
    val totalWeight: Double,
    val percentage: Float
)

/**
 * Статистика по месяцам.
 */
data class MonthlyStat(
    val month: String,
    val monthNumber: Int,
    val count: Int,
    val totalWeight: Double
)

/**
 * Статистика по местам.
 */
data class LocationStat(
    val locationName: String,
    val count: Int,
    val percentage: Float
)
