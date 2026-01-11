package com.example.trophy.presentation.util

/**
 * Утилиты для валидации пользовательского ввода.
 */
object ValidationUtils {

    // Максимальные значения
    const val MAX_WEIGHT_KG = 500.0
    const val MAX_LENGTH_CM = 500.0
    const val MAX_QUANTITY = 100

    /**
     * Результат валидации числового значения.
     */
    data class ValidationResult(
        val filteredValue: String,
        val error: String? = null
    )

    /**
     * Валидация веса.
     * @param input Введённая строка
     * @return ValidationResult с отфильтрованным значением и возможной ошибкой
     */
    fun validateWeight(input: String): ValidationResult {
        val filtered = input.filter { it.isDigit() || it == '.' }
        val value = filtered.toDoubleOrNull()

        val error = when {
            filtered.isBlank() -> null
            value == null -> "Некорректное значение"
            value <= 0 -> "Вес должен быть больше 0"
            value > MAX_WEIGHT_KG -> "Максимальный вес ${MAX_WEIGHT_KG.toInt()} кг"
            else -> null
        }

        return ValidationResult(filtered, error)
    }

    /**
     * Валидация длины.
     * @param input Введённая строка
     * @return ValidationResult с отфильтрованным значением и возможной ошибкой
     */
    fun validateLength(input: String): ValidationResult {
        val filtered = input.filter { it.isDigit() || it == '.' }
        val value = filtered.toDoubleOrNull()

        val error = when {
            filtered.isBlank() -> null
            value == null -> "Некорректное значение"
            value <= 0 -> "Длина должна быть больше 0"
            value > MAX_LENGTH_CM -> "Максимальная длина ${MAX_LENGTH_CM.toInt()} см"
            else -> null
        }

        return ValidationResult(filtered, error)
    }

    /**
     * Валидация количества.
     * @param input Введённая строка
     * @return ValidationResult с отфильтрованным значением и возможной ошибкой
     */
    fun validateQuantity(input: String): ValidationResult {
        val filtered = input.filter { it.isDigit() }
        val value = filtered.toIntOrNull()

        val error = when {
            filtered.isBlank() -> null
            value == null -> "Некорректное значение"
            value <= 0 -> "Количество должно быть больше 0"
            value > MAX_QUANTITY -> "Максимальное количество $MAX_QUANTITY"
            else -> null
        }

        return ValidationResult(filtered.ifBlank { "1" }, error)
    }

    /**
     * Валидация координат.
     */
    fun validateLatitude(input: String): ValidationResult {
        val filtered = input.filter { it.isDigit() || it == '.' || it == '-' }
        val value = filtered.toDoubleOrNull()

        val error = when {
            filtered.isBlank() -> "Укажите широту"
            value == null -> "Некорректное значение"
            value < -90 || value > 90 -> "Широта должна быть от -90 до 90"
            else -> null
        }

        return ValidationResult(filtered, error)
    }

    fun validateLongitude(input: String): ValidationResult {
        val filtered = input.filter { it.isDigit() || it == '.' || it == '-' }
        val value = filtered.toDoubleOrNull()

        val error = when {
            filtered.isBlank() -> "Укажите долготу"
            value == null -> "Некорректное значение"
            value < -180 || value > 180 -> "Долгота должна быть от -180 до 180"
            else -> null
        }

        return ValidationResult(filtered, error)
    }

    /**
     * Валидация названия (не пустое).
     */
    fun validateName(input: String, fieldName: String = "название"): String? {
        return if (input.isBlank()) "Укажите $fieldName" else null
    }
}
