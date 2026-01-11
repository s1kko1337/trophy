package com.example.trophy.presentation.screens.map

import com.example.trophy.domain.model.Location

/**
 * UI State для экрана карты.
 */
data class MapUiState(
    val locations: List<Location> = emptyList(),
    val selectedLocation: Location? = null,
    val currentLatitude: Double? = null,
    val currentLongitude: Double? = null,
    val isLoadingLocation: Boolean = false,
    val isLoading: Boolean = true,
    val error: String? = null
)
