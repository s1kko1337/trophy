package com.example.trophy.presentation.screens.equipment

import com.example.trophy.domain.model.ActivityType
import com.example.trophy.domain.model.Equipment

/**
 * UI State для экрана снаряжения.
 */
data class EquipmentUiState(
    val fishingEquipment: List<Equipment> = emptyList(),
    val huntingEquipment: List<Equipment> = emptyList(),
    val selectedTab: ActivityType = ActivityType.FISHING,
    val isLoading: Boolean = true,
    val error: String? = null,
    val showDeleteDialog: Boolean = false,
    val equipmentToDelete: Equipment? = null
) {
    val currentEquipment: List<Equipment>
        get() = when (selectedTab) {
            ActivityType.FISHING -> fishingEquipment
            ActivityType.HUNTING -> huntingEquipment
        }
}
