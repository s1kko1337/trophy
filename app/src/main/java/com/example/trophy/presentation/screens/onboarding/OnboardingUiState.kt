package com.example.trophy.presentation.screens.onboarding

import com.example.trophy.domain.model.ActivityType

/**
 * UI State для экрана онбординга.
 */
data class OnboardingUiState(
    val currentPage: Int = 0,
    val selectedActivityType: ActivityType = ActivityType.FISHING,
    val isCompleting: Boolean = false
) {
    val isLastPage: Boolean get() = currentPage >= 2
    val isFirstPage: Boolean get() = currentPage == 0
}

/**
 * Страница онбординга.
 */
data class OnboardingPage(
    val title: String,
    val description: String,
    val iconType: OnboardingIcon
)

enum class OnboardingIcon {
    TROPHY,
    MAP,
    CHART
}
