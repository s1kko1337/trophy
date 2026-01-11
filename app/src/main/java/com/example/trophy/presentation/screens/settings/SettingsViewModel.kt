package com.example.trophy.presentation.screens.settings

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trophy.domain.repository.CatchRepository
import com.example.trophy.domain.repository.EquipmentRepository
import com.example.trophy.domain.repository.LocationRepository
import com.example.trophy.service.ExportImportService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel для экрана настроек.
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val exportImportService: ExportImportService,
    private val catchRepository: CatchRepository,
    private val locationRepository: LocationRepository,
    private val equipmentRepository: EquipmentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState = _uiState.asStateFlow()

    fun exportData(uri: Uri) {
        viewModelScope.launch {
            _uiState.update { it.copy(isExporting = true, error = null, exportSuccess = false) }

            exportImportService.exportToUri(uri)
                .onSuccess {
                    _uiState.update { it.copy(isExporting = false, exportSuccess = true) }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(
                            isExporting = false,
                            error = "Ошибка экспорта: ${e.message}"
                        )
                    }
                }
        }
    }

    fun importData(uri: Uri) {
        viewModelScope.launch {
            _uiState.update { it.copy(isImporting = true, error = null, importResult = null) }

            exportImportService.importFromUri(uri)
                .onSuccess { result ->
                    _uiState.update {
                        it.copy(
                            isImporting = false,
                            importResult = ImportResultData(
                                catchesImported = result.catchesImported,
                                locationsImported = result.locationsImported,
                                equipmentImported = result.equipmentImported
                            )
                        )
                    }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(
                            isImporting = false,
                            error = "Ошибка импорта: ${e.message}"
                        )
                    }
                }
        }
    }

    fun showClearDataDialog(show: Boolean) {
        _uiState.update { it.copy(showClearDataDialog = show) }
    }

    fun clearAllData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isClearing = true, showClearDataDialog = false) }

            try {
                // Удаляем все данные
                val catches = catchRepository.getCatches().first()
                catches.forEach { catchItem ->
                    catchRepository.deleteCatch(catchItem)
                }

                val equipment = equipmentRepository.getEquipment().first()
                equipment.forEach { item ->
                    equipmentRepository.deleteEquipment(item)
                }

                val locations = locationRepository.getLocations().first()
                locations.forEach { location ->
                    locationRepository.deleteLocation(location)
                }

                _uiState.update { it.copy(isClearing = false) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isClearing = false,
                        error = "Ошибка очистки: ${e.message}"
                    )
                }
            }
        }
    }

    fun clearExportSuccess() {
        _uiState.update { it.copy(exportSuccess = false) }
    }

    fun clearImportResult() {
        _uiState.update { it.copy(importResult = null) }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
