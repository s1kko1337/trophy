package com.example.trophy.presentation.screens.add_catch

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.net.Uri
import com.example.trophy.domain.model.ActivityType
import com.example.trophy.domain.model.Catch
import com.example.trophy.domain.model.Equipment
import com.example.trophy.domain.model.Location
import com.example.trophy.domain.model.Photo
import com.example.trophy.domain.model.Species
import com.example.trophy.domain.repository.CatchRepository
import com.example.trophy.domain.repository.EquipmentRepository
import com.example.trophy.domain.repository.LocationRepository
import com.example.trophy.domain.repository.PhotoRepository
import com.example.trophy.service.PhotoService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

/**
 * ViewModel для экрана добавления улова/трофея.
 */
@HiltViewModel
class AddCatchViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val catchRepository: CatchRepository,
    private val locationRepository: LocationRepository,
    private val equipmentRepository: EquipmentRepository,
    private val photoRepository: PhotoRepository,
    private val photoService: PhotoService
) : ViewModel() {

    private val activityTypeArg: String = savedStateHandle["activityType"] ?: ActivityType.FISHING.name

    private val _uiState = MutableStateFlow(AddCatchUiState())
    val uiState = _uiState.asStateFlow()

    init {
        val activityType = try {
            ActivityType.valueOf(activityTypeArg)
        } catch (e: IllegalArgumentException) {
            ActivityType.FISHING
        }

        _uiState.update {
            it.copy(
                activityType = activityType,
                speciesSuggestions = Species.getSpeciesByActivityType(activityType)
            )
        }

        loadLocations()
        loadEquipment(activityType)
    }

    private fun loadLocations() {
        viewModelScope.launch {
            locationRepository.getLocations().collect { locations ->
                _uiState.update { it.copy(availableLocations = locations) }
            }
        }
    }

    private fun loadEquipment(activityType: ActivityType) {
        viewModelScope.launch {
            equipmentRepository.getEquipmentByActivityType(activityType).collect { equipment ->
                _uiState.update { it.copy(availableEquipment = equipment) }
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
        // Разрешаем только числа и точку
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
            if (currentList.contains(equipment)) {
                currentList.remove(equipment)
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

    fun onShowCamera(show: Boolean) {
        _uiState.update { it.copy(showCamera = show) }
    }

    fun onPhotoTaken(filePath: String) {
        _uiState.update { state ->
            state.copy(
                photoUris = state.photoUris + filePath,
                showCamera = false
            )
        }
    }

    fun onPhotoFromGallery(uri: Uri) {
        viewModelScope.launch {
            try {
                val filePath = photoService.savePhotoFromUri(uri)
                _uiState.update { state ->
                    state.copy(photoUris = state.photoUris + filePath)
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(error = "Не удалось добавить фото")
                }
            }
        }
    }

    fun onRemovePhoto(filePath: String) {
        photoService.deletePhoto(filePath)
        _uiState.update { state ->
            state.copy(photoUris = state.photoUris.filter { it != filePath })
        }
    }

    fun createTempPhotoFile() = photoService.createTempPhotoFile()

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun saveCatch() {
        val state = _uiState.value

        // Валидация
        if (state.species.isBlank()) {
            _uiState.update { it.copy(speciesError = "Укажите вид") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, error = null) }

            try {
                val catch = Catch(
                    activityType = state.activityType,
                    species = state.species.trim(),
                    weight = state.weightDouble,
                    length = state.lengthDouble,
                    quantity = state.quantityInt,
                    locationId = state.selectedLocation?.id,
                    weatherId = null, // TODO: Добавить погоду
                    notes = state.notes.ifBlank { null },
                    catchDate = state.catchDate,
                    catchTime = state.catchTime,
                    createdAt = LocalDateTime.now(),
                    updatedAt = LocalDateTime.now()
                )

                val catchId = catchRepository.insertCatch(catch)

                // Добавляем связи со снаряжением
                state.selectedEquipment.forEach { equipment ->
                    equipmentRepository.addEquipmentToCatch(catchId, equipment.id)
                }

                // Сохраняем фотографии
                state.photoUris.forEachIndexed { index, filePath ->
                    val photo = Photo(
                        catchId = catchId,
                        filePath = filePath,
                        isPrimary = index == 0 // Первое фото - основное
                    )
                    photoRepository.insertPhoto(photo)
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
