package com.example.trophy.presentation.screens.edit_location

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trophy.domain.model.Location
import com.example.trophy.domain.model.LocationType
import com.example.trophy.domain.repository.LocationRepository
import com.example.trophy.service.LocationService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel для экрана редактирования места.
 */
@HiltViewModel
class EditLocationViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val locationRepository: LocationRepository,
    private val locationService: LocationService
) : ViewModel() {

    private val locationId: Long = savedStateHandle.get<Long>("locationId") ?: 0L

    private val _uiState = MutableStateFlow(EditLocationUiState())
    val uiState = _uiState.asStateFlow()

    private var originalLocation: Location? = null

    init {
        loadLocation()
    }

    private fun loadLocation() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                val location = locationRepository.getLocationById(locationId)
                if (location != null) {
                    originalLocation = location
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            name = location.name,
                            description = location.description ?: "",
                            locationType = location.locationType,
                            latitude = location.latitude,
                            longitude = location.longitude
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = "Место не найдено"
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

    fun onLocationTypeChange(locationType: LocationType) {
        _uiState.update { it.copy(locationType = locationType) }
    }

    fun onLatitudeChange(latitude: String) {
        val lat = latitude.toDoubleOrNull()
        _uiState.update { it.copy(latitude = lat) }
    }

    fun onLongitudeChange(longitude: String) {
        val lon = longitude.toDoubleOrNull()
        _uiState.update { it.copy(longitude = lon) }
    }

    fun setCoordinates(latitude: Double, longitude: Double) {
        _uiState.update {
            it.copy(
                latitude = latitude,
                longitude = longitude
            )
        }
    }

    fun getCurrentLocation() {
        viewModelScope.launch {
            _uiState.update { it.copy(isGettingLocation = true, locationError = null) }

            try {
                val location = locationService.getCurrentLocation()
                if (location != null) {
                    _uiState.update {
                        it.copy(
                            isGettingLocation = false,
                            latitude = location.latitude,
                            longitude = location.longitude
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            isGettingLocation = false,
                            locationError = "Не удалось получить координаты"
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isGettingLocation = false,
                        locationError = e.message ?: "Не удалось получить координаты"
                    )
                }
            }
        }
    }

    fun saveLocation() {
        val state = _uiState.value
        val original = originalLocation

        if (state.name.isBlank()) {
            _uiState.update { it.copy(nameError = "Введите название") }
            return
        }

        if (state.latitude == null || state.longitude == null) {
            _uiState.update { it.copy(locationError = "Укажите координаты") }
            return
        }

        if (original == null) {
            _uiState.update { it.copy(error = "Место не найдено") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, error = null) }

            try {
                val updatedLocation = original.copy(
                    name = state.name.trim(),
                    description = state.description.trim().ifBlank { null },
                    locationType = state.locationType,
                    latitude = state.latitude,
                    longitude = state.longitude
                )

                locationRepository.updateLocation(updatedLocation)

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

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
