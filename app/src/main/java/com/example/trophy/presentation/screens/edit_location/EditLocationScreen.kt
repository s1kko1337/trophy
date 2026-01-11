package com.example.trophy.presentation.screens.edit_location

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MyLocation
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
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.trophy.domain.model.LocationType

/**
 * Экран редактирования места.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditLocationScreen(
    pickedLatitude: Double? = null,
    pickedLongitude: Double? = null,
    onNavigateBack: () -> Unit,
    onNavigateToMapPicker: (lat: Double?, lon: Double?) -> Unit,
    onLocationSaved: () -> Unit,
    viewModel: EditLocationViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()

    var locationTypeExpanded by remember { mutableStateOf(false) }

    // Применяем координаты из MapPicker
    LaunchedEffect(pickedLatitude, pickedLongitude) {
        if (pickedLatitude != null && pickedLongitude != null) {
            viewModel.setCoordinates(pickedLatitude, pickedLongitude)
        }
    }

    // Launcher для запроса разрешений
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineLocationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
        val coarseLocationGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

        if (fineLocationGranted || coarseLocationGranted) {
            viewModel.getCurrentLocation()
        }
    }

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            onLocationSaved()
        }
    }

    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            snackbarHostState.showSnackbar(error)
            viewModel.clearError()
        }
    }

    LaunchedEffect(uiState.locationError) {
        uiState.locationError?.let { error ->
            snackbarHostState.showSnackbar(error)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Редактировать место") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.saveLocation() },
                        enabled = uiState.isValid && !uiState.isSaving && !uiState.isLoading
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
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            else -> {
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
                        label = { Text("Название места") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        isError = uiState.nameError != null,
                        supportingText = uiState.nameError?.let { { Text(it) } }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Тип места
                    ExposedDropdownMenuBox(
                        expanded = locationTypeExpanded,
                        onExpandedChange = { locationTypeExpanded = it }
                    ) {
                        OutlinedTextField(
                            value = uiState.locationType.toDisplayName(),
                            onValueChange = { },
                            readOnly = true,
                            label = { Text("Тип места") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = locationTypeExpanded)
                            }
                        )

                        ExposedDropdownMenu(
                            expanded = locationTypeExpanded,
                            onDismissRequest = { locationTypeExpanded = false }
                        ) {
                            LocationType.entries.forEach { type ->
                                DropdownMenuItem(
                                    text = { Text(type.toDisplayName()) },
                                    onClick = {
                                        viewModel.onLocationTypeChange(type)
                                        locationTypeExpanded = false
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
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2,
                        maxLines = 4
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Координаты
                    Text(
                        text = "Координаты",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Кнопки выбора местоположения
                    Row(modifier = Modifier.fillMaxWidth()) {
                        // Кнопка получения текущего местоположения
                        OutlinedButton(
                            onClick = {
                                locationPermissionLauncher.launch(
                                    arrayOf(
                                        Manifest.permission.ACCESS_FINE_LOCATION,
                                        Manifest.permission.ACCESS_COARSE_LOCATION
                                    )
                                )
                            },
                            modifier = Modifier.weight(1f),
                            enabled = !uiState.isGettingLocation
                        ) {
                            if (uiState.isGettingLocation) {
                                CircularProgressIndicator(
                                    modifier = Modifier.padding(end = 4.dp),
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Icon(
                                    Icons.Default.MyLocation,
                                    contentDescription = null,
                                    modifier = Modifier.padding(end = 4.dp)
                                )
                            }
                            Text("GPS")
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        // Кнопка выбора на карте
                        OutlinedButton(
                            onClick = {
                                onNavigateToMapPicker(uiState.latitude, uiState.longitude)
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                Icons.Default.Map,
                                contentDescription = null,
                                modifier = Modifier.padding(end = 4.dp)
                            )
                            Text("На карте")
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Ручной ввод координат
                    Row(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = uiState.latitude?.toString() ?: "",
                            onValueChange = viewModel::onLatitudeChange,
                            label = { Text("Широта") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        OutlinedTextField(
                            value = uiState.longitude?.toString() ?: "",
                            onValueChange = viewModel::onLongitudeChange,
                            label = { Text("Долгота") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                        )
                    }

                    if (uiState.hasCoordinates) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Координаты установлены",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Кнопка сохранения
                    Button(
                        onClick = { viewModel.saveLocation() },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = uiState.isValid && !uiState.isSaving
                    ) {
                        if (uiState.isSaving) {
                            CircularProgressIndicator(
                                modifier = Modifier.padding(end = 8.dp),
                                strokeWidth = 2.dp
                            )
                        }
                        Text("Сохранить изменения")
                    }
                }
            }
        }
    }
}

/**
 * Отображаемое название типа места.
 */
private fun LocationType.toDisplayName(): String {
    return when (this) {
        LocationType.RIVER -> "Река"
        LocationType.LAKE -> "Озеро"
        LocationType.POND -> "Пруд"
        LocationType.RESERVOIR -> "Водохранилище"
        LocationType.SEA -> "Море"
        LocationType.QUARRY -> "Карьер"
        LocationType.FOREST -> "Лес"
        LocationType.FIELD -> "Поле"
        LocationType.SWAMP -> "Болото"
        LocationType.MEADOW -> "Луг"
        LocationType.MOUNTAINS -> "Горы"
        LocationType.GROUNDS -> "Угодья"
        LocationType.OTHER -> "Другое"
    }
}
