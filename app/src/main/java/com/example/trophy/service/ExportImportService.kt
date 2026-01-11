package com.example.trophy.service

import android.content.Context
import android.net.Uri
import com.example.trophy.domain.model.Catch
import com.example.trophy.domain.model.Equipment
import com.example.trophy.domain.model.Location
import com.example.trophy.domain.repository.CatchRepository
import com.example.trophy.domain.repository.EquipmentRepository
import com.example.trophy.domain.repository.LocationRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.InputStreamReader
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Сервис для экспорта и импорта данных в JSON.
 */
@Singleton
class ExportImportService @Inject constructor(
    @ApplicationContext private val context: Context,
    private val catchRepository: CatchRepository,
    private val locationRepository: LocationRepository,
    private val equipmentRepository: EquipmentRepository
) {
    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }

    /**
     * Экспортирует все данные в JSON строку.
     */
    suspend fun exportToJson(): String {
        val catches = catchRepository.getCatches().first()
        val locations = locationRepository.getLocations().first()
        val equipment = equipmentRepository.getEquipment().first()

        val exportData = ExportData(
            version = 1,
            exportDate = LocalDate.now().toString(),
            catches = catches.map { it.toExportCatch() },
            locations = locations.map { it.toExportLocation() },
            equipment = equipment.map { it.toExportEquipment() }
        )

        return json.encodeToString(exportData)
    }

    /**
     * Экспортирует данные в файл по URI.
     */
    suspend fun exportToUri(uri: Uri): Result<Unit> {
        return try {
            val jsonString = exportToJson()
            context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                outputStream.write(jsonString.toByteArray())
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Импортирует данные из URI.
     */
    suspend fun importFromUri(uri: Uri): Result<ImportResult> {
        return try {
            val jsonString = context.contentResolver.openInputStream(uri)?.use { inputStream ->
                BufferedReader(InputStreamReader(inputStream)).readText()
            } ?: return Result.failure(Exception("Не удалось прочитать файл"))

            val exportData = json.decodeFromString<ExportData>(jsonString)

            var catchesImported = 0
            var locationsImported = 0
            var equipmentImported = 0

            // Импорт мест
            exportData.locations.forEach { exportLocation ->
                val location = exportLocation.toLocation()
                locationRepository.insertLocation(location)
                locationsImported++
            }

            // Импорт снаряжения
            exportData.equipment.forEach { exportEquipment ->
                val equipment = exportEquipment.toEquipment()
                equipmentRepository.insertEquipment(equipment)
                equipmentImported++
            }

            // Импорт уловов
            exportData.catches.forEach { exportCatch ->
                val catch = exportCatch.toCatch()
                catchRepository.insertCatch(catch)
                catchesImported++
            }

            Result.success(
                ImportResult(
                    catchesImported = catchesImported,
                    locationsImported = locationsImported,
                    equipmentImported = equipmentImported
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

/**
 * Результат импорта.
 */
data class ImportResult(
    val catchesImported: Int,
    val locationsImported: Int,
    val equipmentImported: Int
) {
    val total: Int get() = catchesImported + locationsImported + equipmentImported
}

/**
 * Формат экспорта данных.
 */
@Serializable
data class ExportData(
    val version: Int,
    val exportDate: String,
    val catches: List<ExportCatch>,
    val locations: List<ExportLocation>,
    val equipment: List<ExportEquipment>
)

@Serializable
data class ExportCatch(
    val species: String,
    val activityType: String,
    val weight: Double?,
    val length: Double?,
    val quantity: Int,
    val catchDate: String,
    val catchTime: String?,
    val notes: String?
)

@Serializable
data class ExportLocation(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val locationType: String,
    val description: String?
)

@Serializable
data class ExportEquipment(
    val name: String,
    val description: String?,
    val equipmentType: String,
    val activityType: String
)

// Extension functions для конвертации

private fun Catch.toExportCatch() = ExportCatch(
    species = species,
    activityType = activityType.name,
    weight = weight,
    length = length,
    quantity = quantity,
    catchDate = catchDate.toString(),
    catchTime = catchTime?.toString(),
    notes = notes
)

private fun Location.toExportLocation() = ExportLocation(
    name = name,
    latitude = latitude,
    longitude = longitude,
    locationType = locationType.name,
    description = description
)

private fun Equipment.toExportEquipment() = ExportEquipment(
    name = name,
    description = description,
    equipmentType = equipmentType.name,
    activityType = activityType.name
)

private fun ExportCatch.toCatch() = Catch(
    species = species,
    activityType = com.example.trophy.domain.model.ActivityType.valueOf(activityType),
    weight = weight,
    length = length,
    quantity = quantity,
    catchDate = LocalDate.parse(catchDate),
    catchTime = catchTime?.let { LocalTime.parse(it) },
    notes = notes
)

private fun ExportLocation.toLocation() = Location(
    name = name,
    latitude = latitude,
    longitude = longitude,
    locationType = com.example.trophy.domain.model.LocationType.valueOf(locationType),
    description = description
)

private fun ExportEquipment.toEquipment() = Equipment(
    name = name,
    description = description,
    equipmentType = com.example.trophy.domain.model.EquipmentType.valueOf(equipmentType),
    activityType = com.example.trophy.domain.model.ActivityType.valueOf(activityType)
)
