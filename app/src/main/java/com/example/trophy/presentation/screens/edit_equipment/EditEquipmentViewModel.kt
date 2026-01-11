package com.example.trophy.presentation.screens.edit_equipment

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
 * ViewModel для экрана редактирования снаряжения.
 */
@HiltViewModel
class EditEquipmentViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val equipmentRepository: EquipmentRepository
) : ViewModel() {

    private val equipmentId: Long = savedStateHandle.get<Long>("equipmentId") ?: 0L

    private val _uiState = MutableStateFlow(EditEquipmentUiState())
    val uiState = _uiState.asStateFlow()

    private var originalEquipment: Equipment? = null

    init {
        loadEquipment()
    }

    private fun loadEquipment() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                val equipment = equipmentRepository.getEquipmentById(equipmentId)
                if (equipment != null) {
                    originalEquipment = equipment
                    val availableTypes = getTypesForActivity(equipment.activityType)

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            name = equipment.name,
                            description = equipment.description ?: "",
                            equipmentType = equipment.equipmentType,
                            activityType = equipment.activityType,
                            availableTypes = availableTypes
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = "Снаряжение не найдено"
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Ошибка загрузки: ${e.message}"
                    )
                }
            }
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
        val original = originalEquipment

        if (state.name.isBlank()) {
            _uiState.update { it.copy(nameError = "Введите название") }
            return
        }

        if (original == null) {
            _uiState.update { it.copy(error = "Снаряжение не найдено") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, error = null) }

            try {
                val updatedEquipment = original.copy(
                    name = state.name.trim(),
                    description = state.description.ifBlank { null },
                    equipmentType = state.equipmentType
                )

                equipmentRepository.updateEquipment(updatedEquipment)

                _uiState.update { it.copy(isSaving = false, isSaved = true) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        error = "Ошибка сохранения: ${e.message}"
                    )
                }
            }
        }
    }
}
