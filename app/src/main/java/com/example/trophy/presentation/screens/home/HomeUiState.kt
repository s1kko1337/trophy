package com.example.trophy.presentation.screens.home

import com.example.trophy.domain.model.ActivityType
import com.example.trophy.domain.model.Catch
import com.example.trophy.domain.repository.SortOption

/**
 * UI State для главного экрана.
 */
data class HomeUiState(
    val catches: List<Catch> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,
    val filterType: FilterType = FilterType.ALL,
    val sortOption: SortOption = SortOption.DATE_DESC,
    val searchQuery: String = "",
    val totalCatches: Int = 0,
    val totalWeight: Double = 0.0,
    val deletedCatch: Catch? = null
)

/**
 * Типы фильтрации.
 */
enum class FilterType {
    ALL,
    FISHING,
    HUNTING
}

/**
 * Конвертация FilterType в ActivityType.
 */
fun FilterType.toActivityType(): ActivityType? {
    return when (this) {
        FilterType.ALL -> null
        FilterType.FISHING -> ActivityType.FISHING
        FilterType.HUNTING -> ActivityType.HUNTING
    }
}
