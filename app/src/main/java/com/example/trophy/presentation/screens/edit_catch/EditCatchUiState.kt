package com.example.trophy.presentation.screens.edit_catch

import com.example.trophy.domain.model.ActivityType
import com.example.trophy.domain.model.Equipment
import com.example.trophy.domain.model.Location
import java.time.LocalDate
import java.time.LocalTime

/**
 * UI State для экрана редактирования улова/трофея.
 */
data class EditCatchUiState(
    val catchId: Long = 0,
    val activityType: ActivityType = ActivityType.FISHING,
    val species: String = "",
    val speciesError: String? = null,
    val speciesSuggestions: List<String> = emptyList(),
    val showSpeciesSuggestions: Boolean = false,
    val weight: String = "",
    val length: String = "",
    val quantity: String = "1",
    val notes: String = "",
    val catchDate: LocalDate = LocalDate.now(),
    val catchTime: LocalTime? = null,
    val showDatePicker: Boolean = false,
    val showTimePicker: Boolean = false,
    val selectedLocation: Location? = null,
    val availableLocations: List<Location> = emptyList(),
    val showLocationPicker: Boolean = false,
    val selectedEquipment: List<Equipment> = emptyList(),
    val availableEquipment: List<Equipment> = emptyList(),
    val showEquipmentPicker: Boolean = false,
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
    val error: String? = null
) {
    val isValid: Boolean
        get() = species.isNotBlank()

    val weightDouble: Double?
        get() = weight.toDoubleOrNull()

    val lengthDouble: Double?
        get() = length.toDoubleOrNull()

    val quantityInt: Int
        get() = quantity.toIntOrNull() ?: 1
}
