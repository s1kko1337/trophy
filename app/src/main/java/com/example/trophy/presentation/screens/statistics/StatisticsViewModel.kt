package com.example.trophy.presentation.screens.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trophy.domain.model.ActivityType
import com.example.trophy.domain.model.Catch
import com.example.trophy.domain.repository.CatchRepository
import com.example.trophy.domain.repository.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.format.TextStyle
import java.util.Locale
import javax.inject.Inject

/**
 * ViewModel для экрана статистики.
 */
@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val catchRepository: CatchRepository,
    private val locationRepository: LocationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(StatisticsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadStatistics()
    }

    fun onTabSelected(activityType: ActivityType) {
        _uiState.update { it.copy(selectedTab = activityType) }
        loadStatistics()
    }

    private fun loadStatistics() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                val catches: List<Catch> = catchRepository.getCatchesByType(_uiState.value.selectedTab)
                    .first()

                if (catches.isEmpty()) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            totalCatches = 0,
                            totalWeight = 0.0,
                            avgWeight = 0.0,
                            bestCatchWeight = 0.0,
                            bestCatchSpecies = null,
                            speciesStats = emptyList(),
                            monthlyStats = emptyList(),
                            locationStats = emptyList()
                        )
                    }
                    return@launch
                }

                // Общая статистика
                val totalCatches = catches.size
                val totalWeight = catches.mapNotNull { c -> c.weight }.sum()
                val avgWeight = if (catches.any { c -> c.weight != null }) {
                    totalWeight / catches.count { c -> c.weight != null }
                } else 0.0
                val bestCatch = catches.maxByOrNull { c -> c.weight ?: 0.0 }

                // По видам
                val speciesStats = calculateSpeciesStats(catches)

                // По месяцам
                val monthlyStats = calculateMonthlyStats(catches)

                // По местам
                val locationStats = calculateLocationStats(catches)

                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        totalCatches = totalCatches,
                        totalWeight = totalWeight,
                        avgWeight = avgWeight,
                        bestCatchWeight = bestCatch?.weight ?: 0.0,
                        bestCatchSpecies = bestCatch?.species,
                        speciesStats = speciesStats,
                        monthlyStats = monthlyStats,
                        locationStats = locationStats
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Ошибка загрузки статистики"
                    )
                }
            }
        }
    }

    private fun calculateSpeciesStats(catches: List<Catch>): List<SpeciesStat> {
        return catches
            .groupBy { c -> c.species }
            .map { (species, items) ->
                SpeciesStat(
                    species = species,
                    count = items.size,
                    totalWeight = items.mapNotNull { c -> c.weight }.sum(),
                    percentage = items.size.toFloat() / catches.size * 100
                )
            }
            .sortedByDescending { s -> s.count }
            .take(10)
    }

    private fun calculateMonthlyStats(catches: List<Catch>): List<MonthlyStat> {
        return catches
            .groupBy { c -> c.catchDate.monthValue }
            .map { (month, items) ->
                MonthlyStat(
                    month = java.time.Month.of(month)
                        .getDisplayName(TextStyle.SHORT_STANDALONE, Locale("ru")),
                    monthNumber = month,
                    count = items.size,
                    totalWeight = items.mapNotNull { c -> c.weight }.sum()
                )
            }
            .sortedBy { s -> s.monthNumber }
    }

    private suspend fun calculateLocationStats(catches: List<Catch>): List<LocationStat> {
        val catchesWithLocation = catches.filter { c -> c.locationId != null }
        if (catchesWithLocation.isEmpty()) return emptyList()

        return catchesWithLocation
            .groupBy { c -> c.locationId }
            .mapNotNull { (locationId, items) ->
                val location = locationId?.let { id -> locationRepository.getLocationById(id) }
                location?.let { loc ->
                    LocationStat(
                        locationName = loc.name,
                        count = items.size,
                        percentage = items.size.toFloat() / catchesWithLocation.size * 100
                    )
                }
            }
            .sortedByDescending { s -> s.count }
            .take(5)
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun refresh() {
        loadStatistics()
    }
}
