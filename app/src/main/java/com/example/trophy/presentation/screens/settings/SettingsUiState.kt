package com.example.trophy.presentation.screens.settings

/**
 * UI State для экрана настроек.
 */
data class SettingsUiState(
    val isExporting: Boolean = false,
    val isImporting: Boolean = false,
    val exportSuccess: Boolean = false,
    val importResult: ImportResultData? = null,
    val error: String? = null,
    val showClearDataDialog: Boolean = false,
    val isClearing: Boolean = false
)

/**
 * Результат импорта для отображения.
 */
data class ImportResultData(
    val catchesImported: Int,
    val locationsImported: Int,
    val equipmentImported: Int
) {
    val total: Int get() = catchesImported + locationsImported + equipmentImported
}
