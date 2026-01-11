package com.example.trophy.presentation.screens.add_equipment

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trophy.domain.model.ActivityType
import com.example.trophy.domain.model.Equipment
import com.example.trophy.domain.model.EquipmentType
import com.example.trophy.domain.repository.EquipmentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel для экрана добавления снаряжения.
 */
@HiltViewModel
class AddEquipmentViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val equipmentRepository: EquipmentRepository
) : ViewModel() {

    private val activityTypeArg: String = savedStateHandle["activityType"] ?: ActivityType.FISHING.name

    private val _uiState = MutableStateFlow(AddEquipmentUiState())
    val uiState = _uiState.asStateFlow()

    init {
        val activityType = try {
            ActivityType.valueOf(activityTypeArg)
        } catch (e: IllegalArgumentException) {
            ActivityType.FISHING
        }

        val availableTypes = getTypesForActivity(activityType)

        _uiState.update {
            it.copy(
                activityType = activityType,
                availableTypes = availableTypes,
                equipmentType = availableTypes.firstOrNull() ?: EquipmentType.OTHER
            )
        }
    }

    private fun getTypesForActivity(activityType: ActivityType): List<EquipmentType> {
        return when (activityType) {
            ActivityType.FISHING -> listOf(
                EquipmentType.ROD,
                EquipmentType.REEL,
                EquipmentType.LINE,
                EquipmentType.LURE,
                EquipmentType.BAIT,
                EquipmentType.HOOK,
                EquipmentType.FLOAT,
                EquipmentType.NET,
                EquipmentType.OTHER
            )
            ActivityType.HUNTING -> listOf(
                EquipmentType.RIFLE,
                EquipmentType.AMMO,
                EquipmentType.OPTICS,
                EquipmentType.CALL,
                EquipmentType.DECOY,
                EquipmentType.KNIFE,
                EquipmentType.CLOTHING,
                EquipmentType.BACKPACK,
                EquipmentType.OTHER
            )
        }
    }

    fun onNameChange(name: String) {
        _uiState.update {
            it.copy(
                name = name,
                nameError = if (name.isBlank()) "Введите название" else null
            )
        }
    }

    fun onDescriptionChange(description: String) {
        _uiState.update { it.copy(description = description) }
    }

    fun onEquipmentTypeChange(type: EquipmentType) {
        _uiState.update { it.copy(equipmentType = type) }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun saveEquipment() {
        val state = _uiState.value

        if (state.name.isBlank()) {
            _uiState.update { it.copy(nameError = "Введите название") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, error = null) }

            try {
                val equipment = Equipment(
                    name = state.name.trim(),
                    description = state.description.ifBlank { null },
                    equipmentType = state.equipmentType,
                    activityType = state.activityType
                )

                equipmentRepository.insertEquipment(equipment)

                _uiState.update { it.copy(isSaving = false, isSaved = true) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        error = "Не удалось сохранить: ${e.message}"
                    )
                }
            }
        }
    }
}
