package com.example.trophy.presentation.screens.locations

import com.example.trophy.domain.model.Location

/**
 * UI State для экрана списка мест.
 */
data class LocationsUiState(
    val locations: List<Location> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,
    val deletedLocation: Location? = null,
    val showDeleteDialog: Boolean = false,
    val locationToDelete: Location? = null
)
