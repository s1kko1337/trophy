package com.example.trophy.presentation.screens.map

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.trophy.domain.model.Location
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

/**
 * Экран карты с OSMDroid.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    onNavigateBack: () -> Unit,
    onNavigateToAddLocation: (lat: Double?, lon: Double?) -> Unit,
    viewModel: MapViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    // Инициализация OSMDroid
    DisposableEffect(Unit) {
        Configuration.getInstance().userAgentValue = context.packageName
        onDispose { }
    }

    // Launcher для запроса разрешений
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

        if (granted) {
            viewModel.getCurrentLocation()
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
                title = { Text("Карта мест") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        },
        floatingActionButton = {
            Column(horizontalAlignment = Alignment.End) {
                // Кнопка текущего местоположения
                SmallFloatingActionButton(
                    onClick = {
                        if (viewModel.hasLocationPermission()) {
                            viewModel.getCurrentLocation()
                        } else {
                            locationPermissionLauncher.launch(
                                arrayOf(
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                                )
                            )
                        }
                    },
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Icon(Icons.Default.MyLocation, contentDescription = "Моё местоположение")
                }

                // Кнопка добавления места
                FloatingActionButton(
                    onClick = {
                        onNavigateToAddLocation(
                            uiState.currentLatitude,
                            uiState.currentLongitude
                        )
                    }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Добавить место")
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // OSMDroid MapView
            OsmMapView(
                locations = uiState.locations,
                selectedLocation = uiState.selectedLocation,
                currentLatitude = uiState.currentLatitude,
                currentLongitude = uiState.currentLongitude,
                onLocationClick = { viewModel.onLocationSelected(it) },
                modifier = Modifier.fillMaxSize()
            )

            // Информация о выбранном месте
            uiState.selectedLocation?.let { location ->
                LocationInfoCard(
                    location = location,
                    onDismiss = { viewModel.onLocationSelected(null) },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                )
            }
        }
    }
}

/**
 * OSMDroid MapView обёрнутый в Composable.
 */
@Composable
private fun OsmMapView(
    locations: List<Location>,
    selectedLocation: Location?,
    currentLatitude: Double?,
    currentLongitude: Double?,
    onLocationClick: (Location) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    AndroidView(
        factory = { ctx ->
            MapView(ctx).apply {
                setTileSource(TileSourceFactory.MAPNIK)
                setMultiTouchControls(true)
                controller.setZoom(10.0)

                // Центр России по умолчанию
                controller.setCenter(GeoPoint(55.7558, 37.6173))
            }
        },
        update = { mapView ->
            // Очищаем старые маркеры
            mapView.overlays.clear()

            // Добавляем маркеры для всех мест
            locations.forEach { location ->
                val marker = Marker(mapView).apply {
                    position = GeoPoint(location.latitude, location.longitude)
                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    title = location.name
                    snippet = location.description

                    setOnMarkerClickListener { _, _ ->
                        onLocationClick(location)
                        true
                    }
                }
                mapView.overlays.add(marker)
            }

            // Маркер текущего местоположения
            if (currentLatitude != null && currentLongitude != null) {
                val currentMarker = Marker(mapView).apply {
                    position = GeoPoint(currentLatitude, currentLongitude)
                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
                    title = "Моё местоположение"
                }
                mapView.overlays.add(currentMarker)
            }

            // Центрируем на выбранном месте
            selectedLocation?.let { location ->
                mapView.controller.animateTo(
                    GeoPoint(location.latitude, location.longitude),
                    15.0,
                    500L
                )
            } ?: run {
                // Или на текущем местоположении
                if (currentLatitude != null && currentLongitude != null) {
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
 * Карточка с информацией о месте.
 */
@Composable
private fun LocationInfoCard(
    location: Location,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onDismiss,
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = location.name,
                style = MaterialTheme.typography.titleMedium
            )

            location.description?.let { desc ->
                if (desc.isNotBlank()) {
                    Text(
                        text = desc,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Text(
                text = String.format("%.4f, %.4f", location.latitude, location.longitude),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}
