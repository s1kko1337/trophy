package com.example.trophy.service

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Сервис для работы с фотографиями.
 * Управляет сохранением, сжатием и удалением файлов.
 */
@Singleton
class PhotoService @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val photosDir: File by lazy {
        File(context.filesDir, "photos").apply {
            if (!exists()) mkdirs()
        }
    }

    /**
     * Сохраняет фото из URI в внутреннее хранилище.
     * @param uri URI изображения
     * @return путь к сохранённому файлу
     */
    suspend fun savePhotoFromUri(uri: Uri): String {
        val fileName = generateFileName()
        val file = File(photosDir, fileName)

        context.contentResolver.openInputStream(uri)?.use { input ->
            val bitmap = BitmapFactory.decodeStream(input)
            val compressed = compressBitmap(bitmap)
            FileOutputStream(file).use { output ->
                compressed.compress(Bitmap.CompressFormat.JPEG, 85, output)
            }
            bitmap.recycle()
            compressed.recycle()
        }

        return file.absolutePath
    }

    /**
     * Сохраняет Bitmap в внутреннее хранилище.
     * @param bitmap изображение
     * @return путь к сохранённому файлу
     */
    suspend fun saveBitmap(bitmap: Bitmap): String {
        val fileName = generateFileName()
        val file = File(photosDir, fileName)

        val compressed = compressBitmap(bitmap)
        FileOutputStream(file).use { output ->
            compressed.compress(Bitmap.CompressFormat.JPEG, 85, output)
        }
        compressed.recycle()

        return file.absolutePath
    }

    /**
     * Создаёт временный файл для CameraX.
     */
    fun createTempPhotoFile(): File {
        val fileName = generateFileName()
        return File(photosDir, fileName)
    }

    /**
     * Удаляет файл фотографии.
     */
    fun deletePhoto(filePath: String) {
        val file = File(filePath)
        if (file.exists()) {
            file.delete()
        }
    }

    /**
     * Проверяет существование файла.
     */
    fun photoExists(filePath: String): Boolean {
        return File(filePath).exists()
    }

    /**
     * Получает File по пути.
     */
    fun getPhotoFile(filePath: String): File {
        return File(filePath)
    }

    private fun generateFileName(): String {
        val timestamp = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS"))
        return "trophy_$timestamp.jpg"
    }

    private fun compressBitmap(bitmap: Bitmap): Bitmap {
        val maxDimension = 1920

        if (bitmap.width <= maxDimension && bitmap.height <= maxDimension) {
            return bitmap
        }

        val ratio = minOf(
            maxDimension.toFloat() / bitmap.width,
            maxDimension.toFloat() / bitmap.height
        )

        val newWidth = (bitmap.width * ratio).toInt()
        val newHeight = (bitmap.height * ratio).toInt()

        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
    }
}
