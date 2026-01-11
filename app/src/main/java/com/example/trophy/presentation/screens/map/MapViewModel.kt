package com.example.trophy.presentation.screens.map

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trophy.domain.model.Location
import com.example.trophy.domain.repository.LocationRepository
import com.example.trophy.service.LocationService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel для экрана карты.
 */
@HiltViewModel
class MapViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val locationRepository: LocationRepository,
    private val locationService: LocationService
) : ViewModel() {

    // ID места для центрирования (опционально)
    private val selectedLocationId: Long? = savedStateHandle.get<Long>("locationId")

    private val _uiState = MutableStateFlow(MapUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadLocations()
    }

    private fun loadLocations() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                locationRepository.getLocations().collect { locations ->
                    val selectedLocation = selectedLocationId?.let { id ->
                        locations.find { it.id == id }
                    }

                    _uiState.update {
                        it.copy(
                            locations = locations,
                            selectedLocation = selectedLocation,
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Не удалось загрузить места: ${e.message}"
                    )
                }
            }
        }
    }

    fun onLocationSelected(location: Location?) {
        _uiState.update { it.copy(selectedLocation = location) }
    }

    fun hasLocationPermission(): Boolean {
        return locationService.hasLocationPermission()
    }

    fun getCurrentLocation() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingLocation = true) }

            try {
                val location = locationService.getCurrentLocation()
                    ?: locationService.getLastKnownLocation()

                _uiState.update {
                    it.copy(
                        currentLatitude = location?.latitude,
                        currentLongitude = location?.longitude,
                        isLoadingLocation = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoadingLocation = false,
                        error = "Не удалось определить местоположение"
                    )
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
