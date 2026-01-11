package com.example.trophy.presentation.screens.equipment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trophy.domain.model.ActivityType
import com.example.trophy.domain.model.Equipment
import com.example.trophy.domain.repository.EquipmentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel для экрана снаряжения.
 */
@HiltViewModel
class EquipmentViewModel @Inject constructor(
    private val equipmentRepository: EquipmentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EquipmentUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadEquipment()
    }

    private fun loadEquipment() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                // Загружаем снаряжение для рыбалки
                launch {
                    equipmentRepository.getEquipmentByActivityType(ActivityType.FISHING)
                        .collect { equipment ->
                            _uiState.update { it.copy(fishingEquipment = equipment) }
                        }
                }

                // Загружаем снаряжение для охоты
                launch {
                    equipmentRepository.getEquipmentByActivityType(ActivityType.HUNTING)
                        .collect { equipment ->
                            _uiState.update { it.copy(huntingEquipment = equipment, isLoading = false) }
                        }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Не удалось загрузить снаряжение"
                    )
                }
            }
        }
    }

    fun onTabSelected(activityType: ActivityType) {
        _uiState.update { it.copy(selectedTab = activityType) }
    }

    fun onShowDeleteDialog(equipment: Equipment?) {
        _uiState.update {
            it.copy(
                showDeleteDialog = equipment != null,
                equipmentToDelete = equipment
            )
        }
    }

    fun deleteEquipment() {
        val equipment = _uiState.value.equipmentToDelete ?: return

        viewModelScope.launch {
            try {
                equipmentRepository.deleteEquipment(equipment)
                _uiState.update {
                    it.copy(showDeleteDialog = false, equipmentToDelete = null)
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        error = "Не удалось удалить: ${e.message}",
                        showDeleteDialog = false,
                        equipmentToDelete = null
                    )
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
