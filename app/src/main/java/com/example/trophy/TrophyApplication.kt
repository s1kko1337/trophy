package com.example.trophy

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Главный класс приложения.
 * Инициализация Hilt для Dependency Injection.
 */
@HiltAndroidApp
class TrophyApplication : Application()
