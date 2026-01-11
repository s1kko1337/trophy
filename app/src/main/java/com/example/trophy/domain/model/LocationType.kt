package com.example.trophy.domain.model

/**
 * Тип местоположения/угодий.
 */
enum class LocationType {
    // Рыбалка
    RIVER,      // Река
    LAKE,       // Озеро
    POND,       // Пруд
    RESERVOIR,  // Водохранилище
    SEA,        // Море
    QUARRY,     // Карьер

    // Охота
    FOREST,     // Лес
    FIELD,      // Поле
    SWAMP,      // Болото
    MEADOW,     // Луг
    MOUNTAINS,  // Горы
    GROUNDS,    // Угодья

    OTHER       // Другое
}
