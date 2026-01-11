package com.example.trophy.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trophy.domain.model.Catch
import com.example.trophy.domain.repository.CatchRepository
import com.example.trophy.domain.repository.SortOption
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel для главного экрана.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val catchRepository: CatchRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadCatches()
    }

    private fun loadCatches() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                val activityType = _uiState.value.filterType.toActivityType()
                val catchesFlow = if (activityType != null) {
                    catchRepository.getCatchesByType(activityType)
                } else {
                    catchRepository.getCatches()
                }

                catchesFlow.collect { catches ->
                    val sortedCatches = sortCatches(catches, _uiState.value.sortOption)
                    val filteredCatches = filterBySearchQuery(sortedCatches, _uiState.value.searchQuery)

                    _uiState.update {
                        it.copy(
                            catches = filteredCatches,
                            isLoading = false,
                            totalCatches = catches.size,
                            totalWeight = catches.mapNotNull { catch -> catch.weight }.sum()
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Не удалось загрузить записи: ${e.message}"
                    )
                }
            }
        }
    }

    fun onFilterChange(filterType: FilterType) {
        _uiState.update { it.copy(filterType = filterType) }
        loadCatches()
    }

    fun onSortOptionChange(sortOption: SortOption) {
        _uiState.update { state ->
            val sortedCatches = sortCatches(state.catches, sortOption)
            state.copy(
                sortOption = sortOption,
                catches = sortedCatches
            )
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        loadCatches()
    }

    fun onDeleteCatch(catch: Catch) {
        // Убираем из списка и сохраняем для возможного восстановления
        _uiState.update { state ->
            state.copy(
                catches = state.catches.filter { it.id != catch.id },
                deletedCatch = catch
            )
        }
    }

    fun undoDelete() {
        _uiState.update { state ->
            val deletedCatch = state.deletedCatch
            if (deletedCatch != null) {
                state.copy(
                    catches = (state.catches + deletedCatch).sortedByDescending { it.catchDate },
                    deletedCatch = null
                )
            } else {
                state.copy(deletedCatch = null)
            }
        }
    }

    fun confirmDelete() {
        viewModelScope.launch {
            _uiState.value.deletedCatch?.let { catch ->
                try {
                    catchRepository.deleteCatch(catch)
                } catch (e: Exception) {
                    _uiState.update {
                        it.copy(error = "Не удалось удалить запись: ${e.message}")
                    }
                }
            }
            _uiState.update { it.copy(deletedCatch = null) }
        }
    }

    fun onRefresh() {
        loadCatches()
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    private fun sortCatches(catches: List<Catch>, sortOption: SortOption): List<Catch> {
        return when (sortOption) {
            SortOption.DATE_DESC -> catches.sortedByDescending { it.catchDate }
            SortOption.DATE_ASC -> catches.sortedBy { it.catchDate }
            SortOption.WEIGHT_DESC -> catches.sortedByDescending { it.weight ?: 0.0 }
            SortOption.WEIGHT_ASC -> catches.sortedBy { it.weight ?: 0.0 }
            SortOption.SPECIES -> catches.sortedBy { it.species }
        }
    }

    private fun filterBySearchQuery(catches: List<Catch>, query: String): List<Catch> {
        if (query.isBlank()) return catches
        return catches.filter { catch ->
            catch.species.contains(query, ignoreCase = true) ||
                catch.notes?.contains(query, ignoreCase = true) == true
        }
    }
}
