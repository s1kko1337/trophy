package com.example.trophy.presentation.screens.edit_catch

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trophy.domain.model.Catch
import com.example.trophy.domain.model.Equipment
import com.example.trophy.domain.model.Location
import com.example.trophy.domain.model.Species
import com.example.trophy.domain.repository.CatchRepository
import com.example.trophy.domain.repository.EquipmentRepository
import com.example.trophy.domain.repository.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.LocalDate
import javax.inject.Inject

/**
 * ViewModel для экрана редактирования улова/трофея.
 */
@HiltViewModel
class EditCatchViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val catchRepository: CatchRepository,
    private val locationRepository: LocationRepository,
    private val equipmentRepository: EquipmentRepository
) : ViewModel() {

    private val catchId: Long = savedStateHandle["catchId"] ?: 0L

    private val _uiState = MutableStateFlow(EditCatchUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadCatch()
    }

    private fun loadCatch() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                val catch = catchRepository.getCatchById(catchId)

                if (catch == null) {
                    _uiState.update {
                        it.copy(isLoading = false, error = "Запись не найдена")
                    }
                    return@launch
                }

                // Загружаем место
                val location = catch.locationId?.let { locationRepository.getLocationById(it) }

                // Загружаем снаряжение для этого улова
                val selectedEquipment = equipmentRepository.getEquipmentByCatchId(catchId).first()

                // Загружаем доступные места и снаряжение
                val locations = locationRepository.getLocations().first()
                val availableEquipment = equipmentRepository.getEquipmentByActivityType(catch.activityType).first()

                _uiState.update {
                    it.copy(
                        catchId = catch.id,
                        activityType = catch.activityType,
                        species = catch.species,
                        speciesSuggestions = Species.getSpeciesByActivityType(catch.activityType),
                        weight = catch.weight?.toString() ?: "",
                        length = catch.length?.toString() ?: "",
                        quantity = catch.quantity.toString(),
                        notes = catch.notes ?: "",
                        catchDate = catch.catchDate,
                        catchTime = catch.catchTime,
                        selectedLocation = location,
                        availableLocations = locations,
                        selectedEquipment = selectedEquipment,
                        availableEquipment = availableEquipment,
                        isLoading = false
                    )
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

    fun onSpeciesChange(species: String) {
        val suggestions = if (species.length >= 2) {
            _uiState.value.speciesSuggestions.filter {
                it.contains(species, ignoreCase = true)
            }
        } else {
            emptyList()
        }

        _uiState.update {
            it.copy(
                species = species,
                speciesError = if (species.isBlank()) "Укажите вид" else null,
                showSpeciesSuggestions = suggestions.isNotEmpty() && species.isNotBlank()
            )
        }
    }

    fun onSpeciesSelected(species: String) {
        _uiState.update {
            it.copy(
                species = species,
                speciesError = null,
                showSpeciesSuggestions = false
            )
        }
    }

    fun onWeightChange(weight: String) {
        val filtered = weight.filter { it.isDigit() || it == '.' }
        _uiState.update { it.copy(weight = filtered) }
    }

    fun onLengthChange(length: String) {
        val filtered = length.filter { it.isDigit() || it == '.' }
        _uiState.update { it.copy(length = filtered) }
    }

    fun onQuantityChange(quantity: String) {
        val filtered = quantity.filter { it.isDigit() }
        _uiState.update { it.copy(quantity = filtered.ifBlank { "1" }) }
    }

    fun onNotesChange(notes: String) {
        _uiState.update { it.copy(notes = notes) }
    }

    fun onDateChange(date: LocalDate) {
        _uiState.update { it.copy(catchDate = date, showDatePicker = false) }
    }

    fun onTimeChange(time: LocalTime?) {
        _uiState.update { it.copy(catchTime = time, showTimePicker = false) }
    }

    fun onShowDatePicker(show: Boolean) {
        _uiState.update { it.copy(showDatePicker = show) }
    }

    fun onShowTimePicker(show: Boolean) {
        _uiState.update { it.copy(showTimePicker = show) }
    }

    fun onLocationSelected(location: Location?) {
        _uiState.update { it.copy(selectedLocation = location, showLocationPicker = false) }
    }

    fun onShowLocationPicker(show: Boolean) {
        _uiState.update { it.copy(showLocationPicker = show) }
    }

    fun onEquipmentToggle(equipment: Equipment) {
        _uiState.update { state ->
            val currentList = state.selectedEquipment.toMutableList()
            if (currentList.any { it.id == equipment.id }) {
                currentList.removeAll { it.id == equipment.id }
            } else {
                currentList.add(equipment)
            }
            state.copy(selectedEquipment = currentList)
        }
    }

    fun onShowEquipmentPicker(show: Boolean) {
        _uiState.update { it.copy(showEquipmentPicker = show) }
    }

    fun onDismissSpeciesSuggestions() {
        _uiState.update { it.copy(showSpeciesSuggestions = false) }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun saveCatch() {
        val state = _uiState.value

        if (state.species.isBlank()) {
            _uiState.update { it.copy(speciesError = "Укажите вид") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, error = null) }

            try {
                val catch = Catch(
                    id = state.catchId,
                    activityType = state.activityType,
                    species = state.species.trim(),
                    weight = state.weightDouble,
                    length = state.lengthDouble,
                    quantity = state.quantityInt,
                    locationId = state.selectedLocation?.id,
                    weatherId = null,
                    notes = state.notes.ifBlank { null },
                    catchDate = state.catchDate,
                    catchTime = state.catchTime,
                    createdAt = LocalDateTime.now(),
                    updatedAt = LocalDateTime.now()
                )

                catchRepository.updateCatch(catch)

                // Обновляем связи со снаряжением
                equipmentRepository.clearEquipmentFromCatch(state.catchId)
                state.selectedEquipment.forEach { equipment ->
                    equipmentRepository.addEquipmentToCatch(state.catchId, equipment.id)
                }

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
