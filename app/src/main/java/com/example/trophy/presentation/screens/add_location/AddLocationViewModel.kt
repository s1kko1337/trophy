package com.example.trophy.presentation.screens.add_location

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
import java.time.LocalDateTime
import javax.inject.Inject

/**
 * ViewModel для экрана добавления места.
 */
@HiltViewModel
class AddLocationViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val locationRepository: LocationRepository,
    private val locationService: LocationService
) : ViewModel() {

    // Координаты переданные из карты (опционально)
    private val latArg: Double? = savedStateHandle.get<String>("lat")?.toDoubleOrNull()
    private val lonArg: Double? = savedStateHandle.get<String>("lon")?.toDoubleOrNull()

    private val _uiState = MutableStateFlow(AddLocationUiState())
    val uiState = _uiState.asStateFlow()

    init {
        // Если переданы координаты с карты
        if (latArg != null && lonArg != null) {
            _uiState.update {
                it.copy(
                    latitude = latArg,
                    longitude = lonArg
                )
            }
        }
    }

    fun onNameChange(name: String) {
        _uiState.update {
            it.copy(
                name = name,
                nameError = if (name.isBlank()) "Введите название места" else null
            )
        }
    }

    fun onDescriptionChange(description: String) {
        _uiState.update { it.copy(description = description) }
    }

    fun onLocationTypeChange(locationType: LocationType) {
        _uiState.update { it.copy(locationType = locationType) }
    }

    fun onLatitudeChange(lat: String) {
        val latitude = lat.toDoubleOrNull()
        _uiState.update { it.copy(latitude = latitude) }
    }

    fun onLongitudeChange(lon: String) {
        val longitude = lon.toDoubleOrNull()
        _uiState.update { it.copy(longitude = longitude) }
    }

    fun setCoordinates(latitude: Double, longitude: Double) {
        _uiState.update {
            it.copy(
                latitude = latitude,
                longitude = longitude
            )
        }
    }

    fun hasLocationPermission(): Boolean {
        return locationService.hasLocationPermission()
    }

    fun getCurrentLocation() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isGettingLocation = true,
                    locationError = null
                )
            }

            try {
                val location = locationService.getCurrentLocation()

                if (location != null) {
                    _uiState.update {
                        it.copy(
                            latitude = location.latitude,
                            longitude = location.longitude,
                            isGettingLocation = false
                        )
                    }
                } else {
                    // Попробуем последнее известное местоположение
                    val lastLocation = locationService.getLastKnownLocation()
                    if (lastLocation != null) {
                        _uiState.update {
                            it.copy(
                                latitude = lastLocation.latitude,
                                longitude = lastLocation.longitude,
                                isGettingLocation = false
                            )
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                isGettingLocation = false,
                                locationError = "Не удалось определить местоположение"
                            )
                        }
                    }
                }
            } catch (e: SecurityException) {
                _uiState.update {
                    it.copy(
                        isGettingLocation = false,
                        locationError = "Нет разрешения на определение местоположения"
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isGettingLocation = false,
                        locationError = "Ошибка: ${e.message}"
                    )
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null, locationError = null) }
    }

    fun saveLocation() {
        val state = _uiState.value

        if (state.name.isBlank()) {
            _uiState.update { it.copy(nameError = "Введите название места") }
            return
        }

        if (state.latitude == null || state.longitude == null) {
            _uiState.update { it.copy(locationError = "Укажите координаты") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, error = null) }

            try {
                val location = Location(
                    name = state.name.trim(),
                    description = state.description.ifBlank { null },
                    locationType = state.locationType,
                    latitude = state.latitude,
                    longitude = state.longitude,
                    createdAt = LocalDateTime.now()
                )

                locationRepository.insertLocation(location)

                _uiState.update { it.copy(isSaving = false, isSaved = true) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        error = "Не удалось сохранить место: ${e.message}"
                    )
                }
            }
        }
    }
}
