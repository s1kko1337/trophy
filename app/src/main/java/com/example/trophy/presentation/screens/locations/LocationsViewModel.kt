package com.example.trophy.presentation.screens.locations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trophy.domain.model.Location
import com.example.trophy.domain.repository.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel для экрана списка мест.
 */
@HiltViewModel
class LocationsViewModel @Inject constructor(
    private val locationRepository: LocationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LocationsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadLocations()
    }

    private fun loadLocations() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                locationRepository.getLocations().collect { locations ->
                    _uiState.update {
                        it.copy(
                            locations = locations,
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

    fun onDeleteClick(location: Location) {
        _uiState.update {
            it.copy(
                showDeleteDialog = true,
                locationToDelete = location
            )
        }
    }

    fun onDismissDeleteDialog() {
        _uiState.update {
            it.copy(
                showDeleteDialog = false,
                locationToDelete = null
            )
        }
    }

    fun onConfirmDelete() {
        viewModelScope.launch {
            _uiState.value.locationToDelete?.let { location ->
                try {
                    locationRepository.deleteLocation(location)
                    _uiState.update {
                        it.copy(
                            showDeleteDialog = false,
                            locationToDelete = null,
                            deletedLocation = location
                        )
                    }
                } catch (e: Exception) {
                    _uiState.update {
                        it.copy(
                            showDeleteDialog = false,
                            locationToDelete = null,
                            error = "Не удалось удалить место: ${e.message}"
                        )
                    }
                }
            }
        }
    }

    fun onRefresh() {
        loadLocations()
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun clearDeletedLocation() {
        _uiState.update { it.copy(deletedLocation = null) }
    }
}
