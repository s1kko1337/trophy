package com.example.trophy.service

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * Сервис для получения текущего местоположения.
 */
@Singleton
class LocationService @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    /**
     * Проверяет, есть ли разрешения на получение местоположения.
     */
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

    companion object {
        private const val LOCATION_TIMEOUT_MS = 30_000L // 30 секунд таймаут
    }

    /**
     * Получает текущее местоположение с таймаутом.
     * При таймауте возвращает последнее известное местоположение.
     * @return Location или null если не удалось получить
     * @throws SecurityException если нет разрешений
     */
    suspend fun getCurrentLocation(): Location? {
        if (!hasLocationPermission()) {
            throw SecurityException("Нет разрешения на получение местоположения")
        }

        // Пытаемся получить точное местоположение с таймаутом
        val currentLocation = withTimeoutOrNull(LOCATION_TIMEOUT_MS) {
            getCurrentLocationInternal()
        }

        // Если таймаут или ошибка, пробуем получить последнее известное местоположение
        return currentLocation ?: getLastKnownLocation()
    }

    /**
     * Внутренний метод получения текущего местоположения без таймаута.
     */
    private suspend fun getCurrentLocationInternal(): Location? {
        return suspendCancellableCoroutine { continuation ->
            val cancellationTokenSource = CancellationTokenSource()

            try {
                fusedLocationClient.getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    cancellationTokenSource.token
                ).addOnSuccessListener { location ->
                    continuation.resume(location)
                }.addOnFailureListener { exception ->
                    // При ошибке возвращаем null вместо исключения
                    continuation.resume(null)
                }

                continuation.invokeOnCancellation {
                    cancellationTokenSource.cancel()
                }
            } catch (e: SecurityException) {
                continuation.resumeWithException(e)
            }
        }
    }

    /**
     * Получает последнее известное местоположение (быстрее, но может быть неточным).
     */
    suspend fun getLastKnownLocation(): Location? {
        if (!hasLocationPermission()) {
            throw SecurityException("Нет разрешения на получение местоположения")
        }

        return suspendCancellableCoroutine { continuation ->
            try {
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location ->
                        continuation.resume(location)
                    }
                    .addOnFailureListener { exception ->
                        continuation.resumeWithException(exception)
                    }
            } catch (e: SecurityException) {
                continuation.resumeWithException(e)
            }
        }
    }
}
