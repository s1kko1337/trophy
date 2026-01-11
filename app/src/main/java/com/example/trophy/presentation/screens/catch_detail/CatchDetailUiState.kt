package com.example.trophy.presentation.screens.catch_detail

import com.example.trophy.domain.model.Catch
import com.example.trophy.domain.model.Equipment
import com.example.trophy.domain.model.Location
import com.example.trophy.domain.model.Photo
import com.example.trophy.domain.model.Weather

/**
 * UI State для экрана детального просмотра улова/трофея.
 */
data class CatchDetailUiState(
    val catch: Catch? = null,
    val location: Location? = null,
    val weather: Weather? = null,
    val photos: List<Photo> = emptyList(),
    val equipment: List<Equipment> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,
    val isDeleted: Boolean = false,
    val showDeleteDialog: Boolean = false
)
