package com.example.trophy.presentation.screens.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trophy.data.local.datastore.SettingsDataStore
import com.example.trophy.domain.model.ActivityType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel для экрана онбординга.
 */
@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val settingsDataStore: SettingsDataStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState = _uiState.asStateFlow()

    val pages = listOf(
        OnboardingPage(
            title = "Добро пожаловать в Трофей!",
            description = "Приложение для учёта уловов рыбалки и трофеев охоты. " +
                    "Ведите журнал своих успехов и анализируйте статистику.",
            iconType = OnboardingIcon.TROPHY
        ),
        OnboardingPage(
            title = "Отмечайте места",
            description = "Сохраняйте координаты удачных мест на карте. " +
                    "Используйте офлайн-карты для навигации без интернета.",
            iconType = OnboardingIcon.MAP
        ),
        OnboardingPage(
            title = "Анализируйте успехи",
            description = "Просматривайте статистику по видам, местам и сезонам. " +
                    "Экспортируйте данные для резервного копирования.",
            iconType = OnboardingIcon.CHART
        )
    )

    fun nextPage() {
        _uiState.update { state ->
            if (!state.isLastPage) {
                state.copy(currentPage = state.currentPage + 1)
            } else {
                state
            }
        }
    }

    fun previousPage() {
        _uiState.update { state ->
            if (!state.isFirstPage) {
                state.copy(currentPage = state.currentPage - 1)
            } else {
                state
            }
        }
    }

    fun goToPage(page: Int) {
        _uiState.update { it.copy(currentPage = page.coerceIn(0, 2)) }
    }

    fun selectActivityType(activityType: ActivityType) {
        _uiState.update { it.copy(selectedActivityType = activityType) }
    }

    fun completeOnboarding(onComplete: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isCompleting = true) }

            // Сохраняем выбранный тип активности
            settingsDataStore.setLastActivityType(_uiState.value.selectedActivityType)
            // Отмечаем онбординг как пройденный
            settingsDataStore.setOnboardingCompleted(true)

            onComplete()
        }
    }
}
