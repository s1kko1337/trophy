package com.example.trophy.presentation.screens.map

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.launch
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker

/**
 * Экран выбора места на карте.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapPickerScreen(
    initialLatitude: Double?,
    initialLongitude: Double?,
    onLocationSelected: (latitude: Double, longitude: Double) -> Unit,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Выбранные координаты
    var selectedLatitude by remember { mutableStateOf(initialLatitude) }
    var selectedLongitude by remember { mutableStateOf(initialLongitude) }
    var hasSelection by remember { mutableStateOf(initialLatitude != null && initialLongitude != null) }

    // Текущее местоположение
    var currentLatitude by remember { mutableStateOf<Double?>(null) }
    var currentLongitude by remember { mutableStateOf<Double?>(null) }

    // Инициализация OSMDroid
    DisposableEffect(Unit) {
        Configuration.getInstance().userAgentValue = context.packageName
        onDispose { }
    }

    // FusedLocationProviderClient
    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
    }

    fun getCurrentLocation() {
        if (!hasLocationPermission()) return

        try {
            val cancellationTokenSource = CancellationTokenSource()
            fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                cancellationTokenSource.token
            ).addOnSuccessListener { location ->
                location?.let {
                    currentLatitude = it.latitude
                    currentLongitude = it.longitude
                }
            }
        } catch (e: SecurityException) {
            scope.launch {
                snackbarHostState.showSnackbar("Нет разрешения на определение местоположения")
            }
        }
    }

    // Launcher для запроса разрешений
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

        if (granted) {
            getCurrentLocation()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Выберите место на карте") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                },
                actions = {
                    if (hasSelection) {
                        IconButton(
                            onClick = {
                                selectedLatitude?.let { lat ->
                                    selectedLongitude?.let { lon ->
                                        onLocationSelected(lat, lon)
                                    }
                                }
                            }
                        ) {
                            Icon(Icons.Default.Check, contentDescription = "Подтвердить")
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            SmallFloatingActionButton(
                onClick = {
                    if (hasLocationPermission()) {
                        getCurrentLocation()
                    } else {
                        locationPermissionLauncher.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        )
                    }
                }
            ) {
                Icon(Icons.Default.MyLocation, contentDescription = "Моё местоположение")
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Карта с возможностью выбора
            PickerMapView(
                selectedLatitude = selectedLatitude,
                selectedLongitude = selectedLongitude,
                currentLatitude = currentLatitude,
                currentLongitude = currentLongitude,
                onMapClick = { lat, lon ->
                    selectedLatitude = lat
                    selectedLongitude = lon
                    hasSelection = true
                },
                modifier = Modifier.fillMaxSize()
            )

            // Информация о выбранном месте
            if (hasSelection && selectedLatitude != null && selectedLongitude != null) {
                SelectedLocationCard(
                    latitude = selectedLatitude!!,
                    longitude = selectedLongitude!!,
                    onConfirm = {
                        onLocationSelected(selectedLatitude!!, selectedLongitude!!)
                    },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                )
            } else {
                // Подсказка
                Card(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Text(
                        text = "Нажмите на карту, чтобы выбрать место",
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

/**
 * Карта с поддержкой выбора места.
 */
@Composable
private fun PickerMapView(
    selectedLatitude: Double?,
    selectedLongitude: Double?,
    currentLatitude: Double?,
    currentLongitude: Double?,
    onMapClick: (latitude: Double, longitude: Double) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    AndroidView(
        factory = { ctx ->
            MapView(ctx).apply {
                setTileSource(TileSourceFactory.MAPNIK)
                setMultiTouchControls(true)
                controller.setZoom(12.0)

                // Центр по умолчанию (Москва)
                val defaultCenter = GeoPoint(55.7558, 37.6173)
                controller.setCenter(defaultCenter)

                // Обработчик кликов по карте
                val mapEventsReceiver = object : MapEventsReceiver {
                    override fun singleTapConfirmedHelper(p: GeoPoint): Boolean {
                        onMapClick(p.latitude, p.longitude)
                        return true
                    }

                    override fun longPressHelper(p: GeoPoint): Boolean {
                        return false
                    }
                }

                overlays.add(0, MapEventsOverlay(mapEventsReceiver))
            }
        },
        update = { mapView ->
            // Удаляем старые маркеры (кроме overlay для кликов)
            val eventsOverlay = mapView.overlays.firstOrNull { it is MapEventsOverlay }
            mapView.overlays.clear()
            eventsOverlay?.let { mapView.overlays.add(it) }

            // Маркер выбранного места
            if (selectedLatitude != null && selectedLongitude != null) {
                val selectedMarker = Marker(mapView).apply {
                    position = GeoPoint(selectedLatitude, selectedLongitude)
                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    title = "Выбранное место"
                }
                mapView.overlays.add(selectedMarker)

                // Центрируем на выбранном месте
                mapView.controller.animateTo(
                    GeoPoint(selectedLatitude, selectedLongitude),
                    15.0,
                    300L
                )
            }

            // Маркер текущего местоположения (голубой)
            if (currentLatitude != null && currentLongitude != null) {
                val currentMarker = Marker(mapView).apply {
                    position = GeoPoint(currentLatitude, currentLongitude)
                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
                    title = "Моё местоположение"
                }
                mapView.overlays.add(currentMarker)

                // Если нет выбранного места, центрируем на текущем
                if (selectedLatitude == null || selectedLongitude == null) {
                    mapView.controller.animateTo(
                        GeoPoint(currentLatitude, currentLongitude),
                        14.0,
                        500L
                    )
                }
            }

            mapView.invalidate()
        },
        modifier = modifier
    )
}

/**
 * Карточка с выбранными координатами.
 */
@Composable
private fun SelectedLocationCard(
    latitude: Double,
    longitude: Double,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Выбранное место",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = String.format("Широта: %.6f", latitude),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = String.format("Долгота: %.6f", longitude),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onConfirm,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Выбрать это место")
            }
        }
    }
}
