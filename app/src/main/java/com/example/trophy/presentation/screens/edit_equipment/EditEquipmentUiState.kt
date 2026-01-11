package com.example.trophy.presentation.screens.edit_equipment

import com.example.trophy.domain.model.ActivityType
import com.example.trophy.domain.model.EquipmentType

/**
 * UI State для экрана редактирования снаряжения.
 */
data class EditEquipmentUiState(
    val isLoading: Boolean = true,
    val name: String = "",
    val nameError: String? = null,
    val description: String = "",
    val equipmentType: EquipmentType = EquipmentType.OTHER,
    val activityType: ActivityType = ActivityType.FISHING,
    val availableTypes: List<EquipmentType> = emptyList(),
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
    val error: String? = null
) {
    val isValid: Boolean
        get() = name.isNotBlank() && nameError == null
}
