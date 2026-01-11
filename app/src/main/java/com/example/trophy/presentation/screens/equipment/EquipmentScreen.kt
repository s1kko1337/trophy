package com.example.trophy.presentation.screens.equipment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Forest
import androidx.compose.material.icons.outlined.Phishing
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.trophy.domain.model.ActivityType
import com.example.trophy.domain.model.Equipment
import com.example.trophy.domain.model.EquipmentType

/**
 * Экран управления снаряжением.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EquipmentScreen(
    onNavigateBack: () -> Unit,
    onNavigateToAddEquipment: (ActivityType) -> Unit,
    onNavigateToEditEquipment: (Long) -> Unit,
    viewModel: EquipmentViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            snackbarHostState.showSnackbar(error)
            viewModel.clearError()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Снаряжение") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onNavigateToAddEquipment(uiState.selectedTab) }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Добавить")
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Табы для типа активности
            TabRow(
                selectedTabIndex = if (uiState.selectedTab == ActivityType.FISHING) 0 else 1
            ) {
                Tab(
                    selected = uiState.selectedTab == ActivityType.FISHING,
                    onClick = { viewModel.onTabSelected(ActivityType.FISHING) },
                    text = { Text("Рыбалка") },
                    icon = {
                        Icon(Icons.Outlined.Phishing, contentDescription = null)
                    }
                )
                Tab(
                    selected = uiState.selectedTab == ActivityType.HUNTING,
                    onClick = { viewModel.onTabSelected(ActivityType.HUNTING) },
                    text = { Text("Охота") },
                    icon = {
                        Icon(Icons.Outlined.Forest, contentDescription = null)
                    }
                )
            }

            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                uiState.currentEquipment.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Снаряжение не добавлено",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Нажмите + чтобы добавить",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(uiState.currentEquipment, key = { it.id }) { equipment ->
                            EquipmentCard(
                                equipment = equipment,
                                onEditClick = { onNavigateToEditEquipment(equipment.id) },
                                onDeleteClick = { viewModel.onShowDeleteDialog(equipment) }
                            )
                        }
                    }
                }
            }
        }
    }

    // Диалог подтверждения удаления
    if (uiState.showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.onShowDeleteDialog(null) },
            title = { Text("Удалить снаряжение?") },
            text = {
                Text("\"${uiState.equipmentToDelete?.name}\" будет удалено.")
            },
            confirmButton = {
                TextButton(onClick = { viewModel.deleteEquipment() }) {
                    Text("Удалить", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onShowDeleteDialog(null) }) {
                    Text("Отмена")
                }
            }
        )
    }
}

@Composable
private fun EquipmentCard(
    equipment: Equipment,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onEditClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = equipment.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = equipment.equipmentType.toDisplayName(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )

                equipment.description?.let { desc ->
                    if (desc.isNotBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = desc,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            IconButton(onClick = onEditClick) {
                Icon(
                    Icons.Default.Edit,
                    contentDescription = "Редактировать",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            IconButton(onClick = onDeleteClick) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Удалить",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

/**
 * Отображаемое название типа снаряжения.
 */
private fun EquipmentType.toDisplayName(): String {
    return when (this) {
        // Рыбалка
        EquipmentType.ROD -> "Удочка/спиннинг"
        EquipmentType.REEL -> "Катушка"
        EquipmentType.LINE -> "Леска/шнур"
        EquipmentType.LURE -> "Приманка"
        EquipmentType.BAIT -> "Наживка"
        EquipmentType.HOOK -> "Крючок"
        EquipmentType.FLOAT -> "Поплавок"
        EquipmentType.NET -> "Подсачек"
        // Охота
        EquipmentType.RIFLE -> "Ружьё/карабин"
        EquipmentType.AMMO -> "Патроны"
        EquipmentType.OPTICS -> "Оптика"
        EquipmentType.CALL -> "Манок"
        EquipmentType.DECOY -> "Чучело/приманка"
        EquipmentType.KNIFE -> "Нож"
        EquipmentType.CLOTHING -> "Одежда"
        EquipmentType.BACKPACK -> "Рюкзак"
        // Общее
        EquipmentType.OTHER -> "Прочее"
    }
}
