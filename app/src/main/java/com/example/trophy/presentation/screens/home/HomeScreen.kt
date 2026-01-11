package com.example.trophy.presentation.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Forest
import androidx.compose.material.icons.outlined.Phishing
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberDrawerState
import com.example.trophy.domain.repository.SortOption
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.trophy.domain.model.ActivityType
import com.example.trophy.domain.model.Catch
import com.example.trophy.presentation.components.EmptyState
import com.example.trophy.presentation.components.SwipeableCatchCard
import kotlinx.coroutines.launch

/**
 * Главный экран приложения с лентой записей.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToCatchDetail: (Long) -> Unit,
    onNavigateToAddCatch: (ActivityType) -> Unit,
    onNavigateToGallery: () -> Unit,
    onNavigateToMap: () -> Unit,
    onNavigateToStatistics: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToEquipment: () -> Unit,
    onNavigateToLocations: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    var isFabExpanded by remember { mutableStateOf(false) }
    var showSearch by remember { mutableStateOf(false) }
    var showSortMenu by remember { mutableStateOf(false) }

    // Показываем ошибку в Snackbar
    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            snackbarHostState.showSnackbar(error)
            viewModel.clearError()
        }
    }

    // Показываем Snackbar с Undo после удаления
    LaunchedEffect(uiState.deletedCatch) {
        uiState.deletedCatch?.let { deletedCatch ->
            val result = snackbarHostState.showSnackbar(
                message = "Запись удалена",
                actionLabel = "Отменить",
                duration = SnackbarDuration.Short
            )
            if (result == SnackbarResult.ActionPerformed) {
                viewModel.undoDelete()
            } else {
                viewModel.confirmDelete()
            }
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    text = "Трофей",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(16.dp)
                )

                NavigationDrawerItem(
                    label = { Text("Снаряжение") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onNavigateToEquipment()
                    }
                )

                NavigationDrawerItem(
                    label = { Text("Места") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onNavigateToLocations()
                    }
                )

                NavigationDrawerItem(
                    label = { Text("Настройки") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onNavigateToSettings()
                    }
                )
            }
        }
    ) {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                TopAppBar(
                    title = { Text("Трофей") },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Меню")
                        }
                    },
                    actions = {
                        IconButton(onClick = { showSearch = !showSearch }) {
                            Icon(
                                if (showSearch) Icons.Default.Close else Icons.Default.Search,
                                contentDescription = "Поиск"
                            )
                        }
                        Box {
                            IconButton(onClick = { showSortMenu = true }) {
                                Icon(Icons.Default.FilterList, contentDescription = "Сортировка")
                            }
                            SortDropdownMenu(
                                expanded = showSortMenu,
                                currentSort = uiState.sortOption,
                                onDismiss = { showSortMenu = false },
                                onSortSelected = { sortOption ->
                                    viewModel.onSortOptionChange(sortOption)
                                    showSortMenu = false
                                }
                            )
                        }
                    },
                    scrollBehavior = scrollBehavior
                )
            },
            floatingActionButton = {
                ExpandableFab(
                    expanded = isFabExpanded,
                    onExpandedChange = { isFabExpanded = it },
                    onFishingClick = {
                        isFabExpanded = false
                        onNavigateToAddCatch(ActivityType.FISHING)
                    },
                    onHuntingClick = {
                        isFabExpanded = false
                        onNavigateToAddCatch(ActivityType.HUNTING)
                    }
                )
            },
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // Поиск и фильтры
                AnimatedVisibility(visible = showSearch) {
                    SearchBar(
                        query = uiState.searchQuery,
                        onQueryChange = viewModel::onSearchQueryChange,
                        onSearch = { },
                        active = false,
                        onActiveChange = { },
                        placeholder = { Text("Поиск по виду...") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        trailingIcon = {
                            if (uiState.searchQuery.isNotEmpty()) {
                                IconButton(onClick = { viewModel.onSearchQueryChange("") }) {
                                    Icon(Icons.Default.Close, contentDescription = "Очистить")
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) { }
                }

                // Фильтр по типу
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = uiState.filterType == FilterType.ALL,
                        onClick = { viewModel.onFilterChange(FilterType.ALL) },
                        label = { Text("Все") }
                    )
                    FilterChip(
                        selected = uiState.filterType == FilterType.FISHING,
                        onClick = { viewModel.onFilterChange(FilterType.FISHING) },
                        label = { Text("Рыбалка") },
                        leadingIcon = {
                            if (uiState.filterType == FilterType.FISHING) {
                                Icon(Icons.Outlined.Phishing, contentDescription = null)
                            }
                        }
                    )
                    FilterChip(
                        selected = uiState.filterType == FilterType.HUNTING,
                        onClick = { viewModel.onFilterChange(FilterType.HUNTING) },
                        label = { Text("Охота") },
                        leadingIcon = {
                            if (uiState.filterType == FilterType.HUNTING) {
                                Icon(Icons.Outlined.Forest, contentDescription = null)
                            }
                        }
                    )
                }

                PullToRefreshBox(
                    isRefreshing = uiState.isLoading,
                    onRefresh = { viewModel.onRefresh() },
                    modifier = Modifier.fillMaxSize()
                ) {
                    when {
                        uiState.isLoading && uiState.catches.isEmpty() -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }

                        uiState.catches.isEmpty() -> {
                            EmptyState(
                                message = if (uiState.searchQuery.isNotEmpty()) "Ничего не найдено" else "Нет записей",
                                subtitle = if (uiState.searchQuery.isNotEmpty()) "Попробуйте изменить запрос" else "Добавьте свой первый улов или трофей",
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        else -> {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(
                                    items = uiState.catches,
                                    key = { it.id }
                                ) { catch ->
                                    SwipeableCatchCard(
                                        catch = catch,
                                        onClick = { onNavigateToCatchDetail(catch.id) },
                                        onDelete = { viewModel.onDeleteCatch(catch) },
                                        modifier = Modifier.animateItem()
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Расширяемая FAB для выбора типа активности.
 */
@Composable
private fun ExpandableFab(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onFishingClick: () -> Unit,
    onHuntingClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Кнопка охоты
        AnimatedVisibility(
            visible = expanded,
            enter = fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut()
        ) {
            ExtendedFloatingActionButton(
                onClick = onHuntingClick,
                icon = { Icon(Icons.Outlined.Forest, contentDescription = null) },
                text = { Text("Охота") },
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }

        // Кнопка рыбалки
        AnimatedVisibility(
            visible = expanded,
            enter = fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut()
        ) {
            ExtendedFloatingActionButton(
                onClick = onFishingClick,
                icon = { Icon(Icons.Outlined.Phishing, contentDescription = null) },
                text = { Text("Рыбалка") },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

        // Главная FAB
        FloatingActionButton(
            onClick = { onExpandedChange(!expanded) }
        ) {
            Icon(
                imageVector = if (expanded) Icons.Default.Close else Icons.Default.Add,
                contentDescription = if (expanded) "Закрыть" else "Добавить"
            )
        }
    }
}

/**
 * Выпадающее меню сортировки.
 */
@Composable
private fun SortDropdownMenu(
    expanded: Boolean,
    currentSort: SortOption,
    onDismiss: () -> Unit,
    onSortSelected: (SortOption) -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss
    ) {
        DropdownMenuItem(
            text = { Text("По дате (новые)") },
            onClick = { onSortSelected(SortOption.DATE_DESC) },
            leadingIcon = {
                if (currentSort == SortOption.DATE_DESC) {
                    Icon(Icons.Default.FilterList, contentDescription = null)
                }
            }
        )
        DropdownMenuItem(
            text = { Text("По дате (старые)") },
            onClick = { onSortSelected(SortOption.DATE_ASC) },
            leadingIcon = {
                if (currentSort == SortOption.DATE_ASC) {
                    Icon(Icons.Default.FilterList, contentDescription = null)
                }
            }
        )
        DropdownMenuItem(
            text = { Text("По весу (больше)") },
            onClick = { onSortSelected(SortOption.WEIGHT_DESC) },
            leadingIcon = {
                if (currentSort == SortOption.WEIGHT_DESC) {
                    Icon(Icons.Default.FilterList, contentDescription = null)
                }
            }
        )
        DropdownMenuItem(
            text = { Text("По весу (меньше)") },
            onClick = { onSortSelected(SortOption.WEIGHT_ASC) },
            leadingIcon = {
                if (currentSort == SortOption.WEIGHT_ASC) {
                    Icon(Icons.Default.FilterList, contentDescription = null)
                }
            }
        )
        DropdownMenuItem(
            text = { Text("По виду (А-Я)") },
            onClick = { onSortSelected(SortOption.SPECIES) },
            leadingIcon = {
                if (currentSort == SortOption.SPECIES) {
                    Icon(Icons.Default.FilterList, contentDescription = null)
                }
            }
        )
    }
}
