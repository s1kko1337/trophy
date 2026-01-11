package com.example.trophy.presentation.screens.edit_location

import com.example.trophy.domain.model.LocationType

/**
 * UI State для экрана редактирования места.
 */
data class EditLocationUiState(
    val isLoading: Boolean = true,
    val name: String = "",
    val nameError: String? = null,
    val description: String = "",
    val locationType: LocationType = LocationType.RIVER,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val isGettingLocation: Boolean = false,
    val locationError: String? = null,
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
    val error: String? = null
) {
    val isValid: Boolean
        get() = name.isNotBlank() && latitude != null && longitude != null

    val hasCoordinates: Boolean
        get() = latitude != null && longitude != null
}
