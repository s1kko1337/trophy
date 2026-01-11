package com.example.trophy.presentation.screens.add_equipment

import com.example.trophy.domain.model.ActivityType
import com.example.trophy.domain.model.EquipmentType

/**
 * UI State для экрана добавления снаряжения.
 */
data class AddEquipmentUiState(
    val activityType: ActivityType = ActivityType.FISHING,
    val name: String = "",
    val description: String = "",
    val equipmentType: EquipmentType = EquipmentType.OTHER,
    val availableTypes: List<EquipmentType> = emptyList(),
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
    val error: String? = null,
    val nameError: String? = null
) {
    val isValid: Boolean
        get() = name.isNotBlank() && nameError == null
}
