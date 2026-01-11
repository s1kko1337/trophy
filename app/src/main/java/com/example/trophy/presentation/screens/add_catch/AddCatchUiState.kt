package com.example.trophy.presentation.screens.add_catch

import com.example.trophy.domain.model.ActivityType
import com.example.trophy.domain.model.Equipment
import com.example.trophy.domain.model.Location
import java.time.LocalDate
import java.time.LocalTime

/**
 * UI State для экрана добавления улова/трофея.
 */
data class AddCatchUiState(
    // Тип активности
    val activityType: ActivityType = ActivityType.FISHING,

    // Основная информация
    val species: String = "",
    val weight: String = "",
    val length: String = "",
    val quantity: String = "1",
    val notes: String = "",

    // Дата и время
    val catchDate: LocalDate = LocalDate.now(),
    val catchTime: LocalTime? = null,
    val showDatePicker: Boolean = false,
    val showTimePicker: Boolean = false,

    // Место
    val selectedLocation: Location? = null,
    val availableLocations: List<Location> = emptyList(),
    val showLocationPicker: Boolean = false,

    // Снаряжение
    val selectedEquipment: List<Equipment> = emptyList(),
    val availableEquipment: List<Equipment> = emptyList(),
    val showEquipmentPicker: Boolean = false,

    // Фотографии
    val photoUris: List<String> = emptyList(),
    val showCamera: Boolean = false,

    // Предложения видов
    val speciesSuggestions: List<String> = emptyList(),
    val showSpeciesSuggestions: Boolean = false,

    // Состояние
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val error: String? = null,
    val isSaved: Boolean = false,

    // Валидация
    val speciesError: String? = null
) {
    val isValid: Boolean
        get() = species.isNotBlank() && speciesError == null

    val weightDouble: Double?
        get() = weight.toDoubleOrNull()

    val lengthDouble: Double?
        get() = length.toDoubleOrNull()

    val quantityInt: Int
        get() = quantity.toIntOrNull() ?: 1
}
