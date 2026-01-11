package com.example.trophy.presentation.screens.photo_viewer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trophy.domain.model.Photo
import com.example.trophy.domain.repository.PhotoRepository
import com.example.trophy.service.PhotoService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel для экрана просмотра фото.
 */
@HiltViewModel
class PhotoViewerViewModel @Inject constructor(
    private val photoRepository: PhotoRepository,
    private val photoService: PhotoService
) : ViewModel() {

    private val _uiState = MutableStateFlow(PhotoViewerUiState())
    val uiState = _uiState.asStateFlow()

    fun loadPhoto(photoId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                val photo = photoRepository.getPhotoById(photoId)

                if (photo != null) {
                    _uiState.update {
                        it.copy(
                            photo = photo,
                            isLoading = false
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = "Фото не найдено"
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

    fun deletePhoto() {
        val photo = _uiState.value.photo ?: return

        viewModelScope.launch {
            try {
                // Удаляем файл
                photoService.deletePhoto(photo.filePath)
                // Удаляем из базы
                photoRepository.deletePhoto(photo)

                _uiState.update { it.copy(isDeleted = true) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(error = "Не удалось удалить: ${e.message}")
                }
            }
        }
    }
}

/**
 * UI State для просмотра фото.
 */
data class PhotoViewerUiState(
    val photo: Photo? = null,
    val isLoading: Boolean = false,
    val isDeleted: Boolean = false,
    val error: String? = null
)
