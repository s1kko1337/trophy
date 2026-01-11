package com.example.trophy.presentation.screens.catch_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trophy.domain.repository.CatchRepository
import com.example.trophy.domain.repository.EquipmentRepository
import com.example.trophy.domain.repository.LocationRepository
import com.example.trophy.domain.repository.PhotoRepository
import com.example.trophy.domain.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel для экрана детального просмотра улова/трофея.
 */
@HiltViewModel
class CatchDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val catchRepository: CatchRepository,
    private val locationRepository: LocationRepository,
    private val weatherRepository: WeatherRepository,
    private val photoRepository: PhotoRepository,
    private val equipmentRepository: EquipmentRepository
) : ViewModel() {

    private val catchId: Long = savedStateHandle["catchId"] ?: 0L

    private val _uiState = MutableStateFlow(CatchDetailUiState())
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

                // Загружаем связанные данные
                val location = catch.locationId?.let { locationRepository.getLocationById(it) }
                val weather = catch.weatherId?.let { weatherRepository.getWeatherById(it) }

                _uiState.update {
                    it.copy(
                        catch = catch,
                        location = location,
                        weather = weather,
                        isLoading = false
                    )
                }

                // Загружаем фото и снаряжение
                loadPhotos()
                loadEquipment()
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

    private fun loadPhotos() {
        viewModelScope.launch {
            photoRepository.getPhotosByCatchId(catchId).collect { photos ->
                _uiState.update { it.copy(photos = photos) }
            }
        }
    }

    private fun loadEquipment() {
        viewModelScope.launch {
            equipmentRepository.getEquipmentByCatchId(catchId).collect { equipment ->
                _uiState.update { it.copy(equipment = equipment) }
            }
        }
    }

    fun onShowDeleteDialog(show: Boolean) {
        _uiState.update { it.copy(showDeleteDialog = show) }
    }

    fun deleteCatch() {
        viewModelScope.launch {
            try {
                _uiState.value.catch?.let { catch ->
                    catchRepository.deleteCatch(catch)
                    _uiState.update { it.copy(isDeleted = true, showDeleteDialog = false) }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        error = "Не удалось удалить: ${e.message}",
                        showDeleteDialog = false
                    )
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun refresh() {
        loadCatch()
    }
}
