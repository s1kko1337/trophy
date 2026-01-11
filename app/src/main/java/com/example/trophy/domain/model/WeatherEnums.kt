package com.example.trophy.domain.model

/**
 * Направление ветра.
 */
enum class WindDirection {
    N,   // Север
    NE,  // Северо-восток
    E,   // Восток
    SE,  // Юго-восток
    S,   // Юг
    SW,  // Юго-запад
    W,   // Запад
    NW   // Северо-запад
}

/**
 * Облачность.
 */
enum class Cloudiness {
    CLEAR,          // Ясно
    PARTLY_CLOUDY,  // Переменная облачность
    CLOUDY,         // Облачно
    OVERCAST        // Пасмурно
}

/**
 * Осадки.
 */
enum class Precipitation {
    NONE,           // Без осадков
    LIGHT_RAIN,     // Небольшой дождь
    RAIN,           // Дождь
    HEAVY_RAIN,     // Сильный дождь
    SNOW            // Снег
}

/**
 * Фаза луны.
 */
enum class MoonPhase {
    NEW,                // Новолуние
    WAXING_CRESCENT,    // Молодая луна
    FIRST_QUARTER,      // Первая четверть
    WAXING_GIBBOUS,     // Прибывающая луна
    FULL,               // Полнолуние
    WANING_GIBBOUS,     // Убывающая луна
    LAST_QUARTER,       // Последняя четверть
    WANING_CRESCENT     // Старая луна
}
