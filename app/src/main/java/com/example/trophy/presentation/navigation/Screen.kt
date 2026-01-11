package com.example.trophy.presentation.navigation

import kotlinx.serialization.Serializable

/**
 * Sealed класс для определения экранов навигации.
 */
sealed interface Screen {

    @Serializable
    data object Home : Screen

    @Serializable
    data class AddCatch(val activityType: String) : Screen

    @Serializable
    data class CatchDetail(val catchId: Long) : Screen

    @Serializable
    data class EditCatch(val catchId: Long) : Screen

    @Serializable
    data object Gallery : Screen

    @Serializable
    data class PhotoViewer(val photoId: Long) : Screen

    @Serializable
    data class MapScreen(val locationId: Long? = null) : Screen

    @Serializable
    data class MapPicker(
        val initialLatitude: Double? = null,
        val initialLongitude: Double? = null
    ) : Screen

    @Serializable
    data object Locations : Screen

    @Serializable
    data class AddLocation(val latitude: Double? = null, val longitude: Double? = null) : Screen

    @Serializable
    data class EditLocation(val locationId: Long) : Screen

    @Serializable
    data object Equipment : Screen

    @Serializable
    data class AddEquipment(val activityType: String) : Screen

    @Serializable
    data class EditEquipment(val equipmentId: Long) : Screen

    @Serializable
    data object Statistics : Screen

    @Serializable
    data object Settings : Screen

    @Serializable
    data object Onboarding : Screen
}
