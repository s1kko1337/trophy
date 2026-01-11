package com.example.trophy.presentation.screens.add_equipment

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.trophy.domain.model.ActivityType
import com.example.trophy.domain.model.EquipmentType

/**
 * Экран добавления снаряжения.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEquipmentScreen(
    activityType: ActivityType,
    onNavigateBack: () -> Unit,
    onEquipmentSaved: () -> Unit,
    viewModel: AddEquipmentViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()

    var typeExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            onEquipmentSaved()
        }
    }

    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            snackbarHostState.showSnackbar(error)
            viewModel.clearError()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (activityType == ActivityType.FISHING) "Снаряжение для рыбалки"
                        else "Снаряжение для охоты"
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.saveEquipment() },
                        enabled = uiState.isValid && !uiState.isSaving
                    ) {
                        if (uiState.isSaving) {
                            CircularProgressIndicator(
                                modifier = Modifier.padding(8.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(Icons.Default.Check, contentDescription = "Сохранить")
                        }
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(16.dp)
                .imePadding()
        ) {
            // Название
            OutlinedTextField(
                value = uiState.name,
                onValueChange = viewModel::onNameChange,
                label = { Text("Название") },
                placeholder = { Text("Например: Shimano Ultegra 4000") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = uiState.nameError != null,
                supportingText = uiState.nameError?.let { { Text(it) } }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Тип снаряжения
            ExposedDropdownMenuBox(
                expanded = typeExpanded,
                onExpandedChange = { typeExpanded = it }
            ) {
                OutlinedTextField(
                    value = uiState.equipmentType.toDisplayName(),
                    onValueChange = { },
                    readOnly = true,
                    label = { Text("Тип снаряжения") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = typeExpanded)
                    }
                )

                ExposedDropdownMenu(
                    expanded = typeExpanded,
                    onDismissRequest = { typeExpanded = false }
                ) {
                    uiState.availableTypes.forEach { type ->
                        DropdownMenuItem(
                            text = { Text(type.toDisplayName()) },
                            onClick = {
                                viewModel.onEquipmentTypeChange(type)
                                typeExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Описание
            OutlinedTextField(
                value = uiState.description,
                onValueChange = viewModel::onDescriptionChange,
                label = { Text("Описание (опционально)") },
                placeholder = { Text("Характеристики, заметки...") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
                maxLines = 4
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Кнопка сохранения
            Button(
                onClick = { viewModel.saveEquipment() },
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState.isValid && !uiState.isSaving
            ) {
                if (uiState.isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(end = 8.dp),
                        strokeWidth = 2.dp
                    )
                }
                Text("Сохранить")
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
