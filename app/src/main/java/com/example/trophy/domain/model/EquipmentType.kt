package com.example.trophy.domain.model

/**
 * Тип снаряжения.
 */
enum class EquipmentType {
    // Рыбалка
    ROD,        // Удочка/спиннинг
    REEL,       // Катушка
    LINE,       // Леска/шнур
    LURE,       // Приманка (блесна, воблер)
    BAIT,       // Наживка
    HOOK,       // Крючок
    FLOAT,      // Поплавок
    NET,        // Подсачек

    // Охота
    RIFLE,      // Ружьё/карабин
    AMMO,       // Патроны
    OPTICS,     // Оптика
    CALL,       // Манок
    DECOY,      // Чучело/приманка
    KNIFE,      // Нож
    CLOTHING,   // Одежда
    BACKPACK,   // Рюкзак

    OTHER       // Прочее
}
