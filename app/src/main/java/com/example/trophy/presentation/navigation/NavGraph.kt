package com.example.trophy.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.trophy.domain.model.ActivityType
import com.example.trophy.presentation.screens.add_catch.AddCatchScreen
import com.example.trophy.presentation.screens.add_equipment.AddEquipmentScreen
import com.example.trophy.presentation.screens.add_location.AddLocationScreen
import com.example.trophy.presentation.screens.catch_detail.CatchDetailScreen
import com.example.trophy.presentation.screens.edit_catch.EditCatchScreen
import com.example.trophy.presentation.screens.edit_equipment.EditEquipmentScreen
import com.example.trophy.presentation.screens.edit_location.EditLocationScreen
import com.example.trophy.presentation.screens.equipment.EquipmentScreen
import com.example.trophy.presentation.screens.gallery.GalleryScreen
import com.example.trophy.presentation.screens.home.HomeScreen
import com.example.trophy.presentation.screens.locations.LocationsScreen
import com.example.trophy.presentation.screens.map.MapPickerScreen
import com.example.trophy.presentation.screens.map.MapScreen
import com.example.trophy.presentation.screens.photo_viewer.PhotoViewerScreen
import com.example.trophy.presentation.screens.onboarding.OnboardingScreen
import com.example.trophy.presentation.screens.settings.SettingsScreen
import com.example.trophy.presentation.screens.statistics.StatisticsScreen

/**
 * Главный граф навигации приложения.
 */
@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: Screen = Screen.Home,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // Главный экран
        composable<Screen.Home> {
            HomeScreen(
                onNavigateToCatchDetail = { catchId ->
                    navController.navigate(Screen.CatchDetail(catchId))
                },
                onNavigateToAddCatch = { activityType ->
                    navController.navigate(Screen.AddCatch(activityType.name))
                },
                onNavigateToGallery = {
                    navController.navigate(Screen.Gallery)
                },
                onNavigateToMap = {
                    navController.navigate(Screen.MapScreen())
                },
                onNavigateToStatistics = {
                    navController.navigate(Screen.Statistics)
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings)
                },
                onNavigateToEquipment = {
                    navController.navigate(Screen.Equipment)
                },
                onNavigateToLocations = {
                    navController.navigate(Screen.Locations)
                }
            )
        }

        // Добавление улова
        composable<Screen.AddCatch> { backStackEntry ->
            val route = backStackEntry.toRoute<Screen.AddCatch>()
            val activityType = try {
                ActivityType.valueOf(route.activityType)
            } catch (e: IllegalArgumentException) {
                ActivityType.FISHING
            }

            AddCatchScreen(
                activityType = activityType,
                onNavigateBack = { navController.popBackStack() },
                onCatchSaved = {
                    navController.popBackStack()
                }
            )
        }

        // Детали улова
        composable<Screen.CatchDetail> { backStackEntry ->
            val route = backStackEntry.toRoute<Screen.CatchDetail>()

            CatchDetailScreen(
                catchId = route.catchId,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToEdit = { catchId ->
                    navController.navigate(Screen.EditCatch(catchId))
                },
                onNavigateToPhotoViewer = { photoId ->
                    navController.navigate(Screen.PhotoViewer(photoId))
                }
            )
        }

        // Редактирование улова
        composable<Screen.EditCatch> { backStackEntry ->
            val route = backStackEntry.toRoute<Screen.EditCatch>()

            EditCatchScreen(
                catchId = route.catchId,
                onNavigateBack = { navController.popBackStack() },
                onCatchSaved = {
                    navController.popBackStack()
                }
            )
        }

        // Галерея
        composable<Screen.Gallery> {
            GalleryScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToPhotoViewer = { photoId ->
                    navController.navigate(Screen.PhotoViewer(photoId))
                }
            )
        }

        // Просмотр фото
        composable<Screen.PhotoViewer> { backStackEntry ->
            val route = backStackEntry.toRoute<Screen.PhotoViewer>()

            PhotoViewerScreen(
                photoId = route.photoId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Карта
        composable<Screen.MapScreen> { backStackEntry ->
            val route = backStackEntry.toRoute<Screen.MapScreen>()

            MapScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToAddLocation = { lat, lon ->
                    navController.navigate(Screen.AddLocation(lat, lon))
                }
            )
        }

        // Список мест
        composable<Screen.Locations> {
            LocationsScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToAddLocation = {
                    navController.navigate(Screen.AddLocation(null, null))
                },
                onNavigateToEditLocation = { locationId ->
                    navController.navigate(Screen.EditLocation(locationId))
                },
                onNavigateToMap = { locationId ->
                    navController.navigate(Screen.MapScreen(locationId))
                }
            )
        }

        // Добавление места
        composable<Screen.AddLocation> { backStackEntry ->
            val route = backStackEntry.toRoute<Screen.AddLocation>()

            // Получаем координаты из savedStateHandle (возврат из MapPicker)
            val pickedLat = backStackEntry.savedStateHandle.get<Double>("picked_latitude")
            val pickedLon = backStackEntry.savedStateHandle.get<Double>("picked_longitude")

            AddLocationScreen(
                initialLatitude = pickedLat ?: route.latitude,
                initialLongitude = pickedLon ?: route.longitude,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToMapPicker = { lat, lon ->
                    navController.navigate(Screen.MapPicker(lat, lon))
                },
                onLocationSaved = {
                    navController.popBackStack()
                }
            )
        }

        // Выбор места на карте
        composable<Screen.MapPicker> { backStackEntry ->
            val route = backStackEntry.toRoute<Screen.MapPicker>()

            MapPickerScreen(
                initialLatitude = route.initialLatitude,
                initialLongitude = route.initialLongitude,
                onLocationSelected = { lat, lon ->
                    // Возвращаем координаты через savedStateHandle
                    navController.previousBackStackEntry?.savedStateHandle?.set("picked_latitude", lat)
                    navController.previousBackStackEntry?.savedStateHandle?.set("picked_longitude", lon)
                    navController.popBackStack()
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Редактирование места
        composable<Screen.EditLocation> { backStackEntry ->
            val route = backStackEntry.toRoute<Screen.EditLocation>()

            // Получаем координаты из savedStateHandle (возврат из MapPicker)
            val pickedLat = backStackEntry.savedStateHandle.get<Double>("picked_latitude")
            val pickedLon = backStackEntry.savedStateHandle.get<Double>("picked_longitude")

            EditLocationScreen(
                pickedLatitude = pickedLat,
                pickedLongitude = pickedLon,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToMapPicker = { lat, lon ->
                    navController.navigate(Screen.MapPicker(lat, lon))
                },
                onLocationSaved = {
                    navController.popBackStack()
                }
            )
        }

        // Снаряжение
        composable<Screen.Equipment> {
            EquipmentScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToAddEquipment = { activityType ->
                    navController.navigate(Screen.AddEquipment(activityType.name))
                },
                // Add this missing parameter:
                onNavigateToEditEquipment = { equipmentId ->
                    navController.navigate(Screen.EditEquipment(equipmentId))
                }
            )
        }

        // Добавление снаряжения
        composable<Screen.AddEquipment> { backStackEntry ->
            val route = backStackEntry.toRoute<Screen.AddEquipment>()
            val activityType = try {
                ActivityType.valueOf(route.activityType)
            } catch (e: IllegalArgumentException) {
                ActivityType.FISHING
            }

            AddEquipmentScreen(
                activityType = activityType,
                onNavigateBack = { navController.popBackStack() },
                onEquipmentSaved = {
                    navController.popBackStack()
                }
            )
        }

        // Редактирование снаряжения
        composable<Screen.EditEquipment> { backStackEntry ->
            val route = backStackEntry.toRoute<Screen.EditEquipment>()

            EditEquipmentScreen(
                onNavigateBack = { navController.popBackStack() },
                onEquipmentSaved = { navController.popBackStack() }
            )
        }

        // Статистика
        composable<Screen.Statistics> {
            StatisticsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Настройки
        composable<Screen.Settings> {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Онбординг
        composable<Screen.Onboarding> {
            OnboardingScreen(
                onComplete = {
                    navController.navigate(Screen.Home) {
                        popUpTo(Screen.Onboarding) { inclusive = true }
                    }
                }
            )
        }
    }
}
