# Техническое задание: «Трофей» — Мониторинг и учёт охоты/рыбалки

**Тип проекта:** Курсовая работа
**Платформа:** Android
**Стек:** Kotlin + Jetpack Compose

---

## 1. Общие технические требования

| №   | Требование                               | Статус |
|-----|------------------------------------------|--------|
| 1.1 | Язык разработки: Kotlin                  | ☐      |
| 1.2 | UI Framework: Jetpack Compose            | ☐      |
| 1.3 | minSdk: 24 (Android 7.0)                 | ☐      |
| 1.4 | targetSdk: 36                            | ☐      |
| 1.5 | Локальное хранение: Room Database        | ☐      |
| 1.6 | Архитектура: MVVM + Clean Architecture   | ☐      |
| 1.7 | DI: Hilt                                 | ☐      |
| 1.8 | Navigation: Jetpack Navigation Compose   | ☐      |
| 1.9 | Core library desugaring (java.time для API < 26) | ☐ |

---

## 2. Функциональные требования

### 2.1 Управление записями уловов/трофеев (CRUD)

| №     | Требование                                              | Статус |
|-------|--------------------------------------------------------|--------|
| 2.1.1 | Создание новой записи об улове/трофее                   | ☐      |
| 2.1.2 | Выбор типа активности (рыбалка/охота)                   | ☐      |
| 2.1.3 | Указание вида рыбы/дичи (выбор из списка + свой вариант)| ☐      |
| 2.1.4 | Указание веса (опционально, в кг)                       | ☐      |
| 2.1.5 | Указание размера/длины (опционально, в см)              | ☐      |
| 2.1.6 | Добавление заметок/описания                             | ☐      |
| 2.1.7 | Редактирование существующей записи                      | ☐      |
| 2.1.8 | Удаление записи                                         | ☐      |
| 2.1.9 | Удаление записи свайпом (swipe-to-delete)               | ☐      |
| 2.1.10| Просмотр списка всех записей                            | ☐      |
| 2.1.11| Просмотр детальной информации о записи                  | ☐      |

### 2.2 Дата и время

| №     | Требование                                              | Статус |
|-------|--------------------------------------------------------|--------|
| 2.2.1 | Установка даты улова/добычи                             | ☐      |
| 2.2.2 | Установка времени (опционально)                         | ☐      |
| 2.2.3 | Просмотр записей по периодам (день, неделя, месяц, год) | ☐      |
| 2.2.4 | Календарный вид с метками дней с уловами                | ☐      |

### 2.3 Геолокация и места

| №     | Требование                                              | Статус |
|-------|--------------------------------------------------------|--------|
| 2.3.1 | Сохранение GPS-координат места                          | ☐      |
| 2.3.2 | Название места (текстовое)                              | ☐      |
| 2.3.3 | Описание места (опционально)                            | ☐      |
| 2.3.4 | Тип водоёма/угодий (река, озеро, пруд, лес, поле и др.) | ☐      |
| 2.3.5 | Список сохранённых мест                                 | ☐      |
| 2.3.6 | Редактирование сохранённых мест                         | ☐      |
| 2.3.7 | Удаление сохранённых мест                               | ☐      |
| 2.3.8 | Отображение мест на карте (OSMDroid)                    | ☐      |
| 2.3.9 | Автоопределение текущего местоположения                 | ☐      |
| 2.3.10| Фильтрация уловов по месту                              | ☐      |
| 2.3.11| Статистика уловистости по местам                        | ☐      |

**Типы мест:**

| Рыбалка      | Охота        |
|--------------|--------------|
| Река         | Лес          |
| Озеро        | Поле         |
| Пруд         | Болото       |
| Водохранилище| Луг          |
| Море         | Горы         |
| Карьер       | Угодья       |

### 2.4 Фотографии

| №     | Требование                                              | Статус |
|-------|--------------------------------------------------------|--------|
| 2.4.1 | Прикрепление фото к записи (несколько фото)             | ☐      |
| 2.4.2 | Съёмка фото через камеру                                | ☐      |
| 2.4.3 | Выбор фото из галереи                                   | ☐      |
| 2.4.4 | Просмотр фото в полноэкранном режиме                    | ☐      |
| 2.4.5 | Удаление фото из записи                                 | ☐      |
| 2.4.6 | Галерея всех трофеев (фото-лента)                       | ☐      |
| 2.4.7 | Фильтрация галереи по типу (рыба/дичь)                  | ☐      |
| 2.4.8 | Сортировка галереи по дате/весу                         | ☐      |
| 2.4.9 | Сжатие фото для экономии места                          | ☐      |
| 2.4.10| Хранение фото в локальной папке приложения              | ☐      |

### 2.5 Погодные условия

| №     | Требование                                              | Статус |
|-------|--------------------------------------------------------|--------|
| 2.5.1 | Указание температуры воздуха (°C)                       | ☐      |
| 2.5.2 | Указание атмосферного давления (мм рт. ст.)             | ☐      |
| 2.5.3 | Указание влажности (%)                                  | ☐      |
| 2.5.4 | Указание скорости ветра (м/с)                           | ☐      |
| 2.5.5 | Указание направления ветра (С, СВ, В, ЮВ, Ю, ЮЗ, З, СЗ) | ☐      |
| 2.5.6 | Указание облачности (ясно, переменная, пасмурно)        | ☐      |
| 2.5.7 | Указание осадков (нет, дождь, снег)                     | ☐      |
| 2.5.8 | Указание фазы луны (опционально)                        | ☐      |
| 2.5.9 | Анализ успешности при разных погодных условиях          | ☐      |

### 2.6 Снаряжение

| №     | Требование                                              | Статус |
|-------|--------------------------------------------------------|--------|
| 2.6.1 | Каталог снаряжения (CRUD)                               | ☐      |
| 2.6.2 | Категории снаряжения (удочки, катушки, приманки, ружья и др.) | ☐ |
| 2.6.3 | Название снаряжения                                     | ☐      |
| 2.6.4 | Описание снаряжения (опционально)                       | ☐      |
| 2.6.5 | Привязка снаряжения к записи об улове                   | ☐      |
| 2.6.6 | Множественный выбор снаряжения для одной записи         | ☐      |
| 2.6.7 | Статистика эффективности снаряжения                     | ☐      |
| 2.6.8 | Фильтрация по использованному снаряжению                | ☐      |

**Категории снаряжения:**

| Рыбалка              | Охота               |
|----------------------|---------------------|
| Удочка/спиннинг      | Ружьё/карабин       |
| Катушка              | Патроны             |
| Леска/шнур           | Оптика              |
| Приманка (блесна, воблер) | Манок          |
| Наживка              | Чучело/приманка     |
| Крючок               | Нож                 |
| Поплавок             | Одежда              |
| Подсачек             | Рюкзак              |

### 2.7 Сортировка и фильтрация

| №     | Требование                                              | Статус |
|-------|--------------------------------------------------------|--------|
| 2.7.1 | Сортировка по дате (новые первые/старые первые)         | ☐      |
| 2.7.2 | Сортировка по весу                                      | ☐      |
| 2.7.3 | Сортировка по виду                                      | ☐      |
| 2.7.4 | Фильтр: только рыбалка                                  | ☐      |
| 2.7.5 | Фильтр: только охота                                    | ☐      |
| 2.7.6 | Фильтр: по виду рыбы/дичи                               | ☐      |
| 2.7.7 | Фильтр: по месту                                        | ☐      |
| 2.7.8 | Фильтр: по периоду (сегодня, неделя, месяц, год, произвольный) | ☐ |
| 2.7.9 | Фильтр: по снаряжению                                   | ☐      |
| 2.7.10| Комбинирование нескольких фильтров                      | ☐      |
| 2.7.11| Поиск по названию вида                                  | ☐      |
| 2.7.12| Сохранение выбранной сортировки                         | ☐      |

### 2.8 Статистика и аналитика

| №     | Требование                                              | Статус |
|-------|--------------------------------------------------------|--------|
| 2.8.1 | Общее количество уловов/трофеев                         | ☐      |
| 2.8.2 | Количество за период (день, неделя, месяц, год)         | ☐      |
| 2.8.3 | Суммарный вес уловов за период                          | ☐      |
| 2.8.4 | Средний вес улова                                       | ☐      |
| 2.8.5 | Рекорды по весу (топ-10 по каждому виду)                | ☐      |
| 2.8.6 | График уловов по месяцам (Vico)                         | ☐      |
| 2.8.7 | Распределение по видам (круговая диаграмма)             | ☐      |
| 2.8.8 | Топ-5 самых уловистых мест                              | ☐      |
| 2.8.9 | Топ-5 самых эффективных снастей                         | ☐      |
| 2.8.10| Сравнение сезонов (весна/лето/осень/зима)               | ☐      |
| 2.8.11| Анализ лучших погодных условий для клёва                | ☐      |
| 2.8.12| Динамика по годам                                       | ☐      |

### 2.9 Экспорт/Импорт данных

| №     | Требование                                              | Статус |
|-------|--------------------------------------------------------|--------|
| 2.9.1 | Экспорт всех данных в JSON                              | ☐      |
| 2.9.2 | Импорт данных из JSON                                   | ☐      |
| 2.9.3 | Выбор места сохранения (Storage Access Framework)       | ☐      |
| 2.9.4 | Валидация JSON при импорте                              | ☐      |
| 2.9.5 | Опции импорта: заменить всё / объединить                | ☐      |
| 2.9.6 | Прогресс-индикатор при импорте/экспорте                 | ☐      |
| 2.9.7 | Экспорт/импорт фотографий (ZIP-архив)                   | ☐      |

### 2.10 Онбординг

| №      | Требование                                             | Статус |
|--------|--------------------------------------------------------|--------|
| 2.10.1 | Приветственный экран (3-5 слайдов)                     | ☐      |
| 2.10.2 | Слайд: Обзор возможностей приложения                   | ☐      |
| 2.10.3 | Слайд: Учёт уловов и трофеев                           | ☐      |
| 2.10.4 | Слайд: Карта и места                                   | ☐      |
| 2.10.5 | Слайд: Галерея и статистика                            | ☐      |
| 2.10.6 | Запрос разрешений (камера, геолокация, хранилище)      | ☐      |
| 2.10.7 | Индикатор прогресса (точки)                            | ☐      |
| 2.10.8 | Кнопка «Пропустить»                                    | ☐      |
| 2.10.9 | Показ только при первом запуске                        | ☐      |

---

## 3. UI/UX требования

### 3.1 Дизайн и темы

| №     | Требование                                              | Статус |
|-------|--------------------------------------------------------|--------|
| 3.1.1 | Material Design 3 (Material You)                        | ☐      |
| 3.1.2 | Светлая тема                                            | ☐      |
| 3.1.3 | Тёмная тема                                             | ☐      |
| 3.1.4 | Переключение темы в настройках                          | ☐      |
| 3.1.5 | Автопереключение по системным настройкам                | ☐      |
| 3.1.6 | Dynamic Color (цвета из обоев, Android 12+)             | ☐      |
| 3.1.7 | Fallback цветовая схема для Android < 12                | ☐      |
| 3.1.8 | Цветовая палитра: природные оттенки (зелёный, коричневый, синий) | ☐ |

### 3.2 Анимации и переходы

| №     | Требование                                              | Статус |
|-------|--------------------------------------------------------|--------|
| 3.2.1 | Анимация переходов между экранами                       | ☐      |
| 3.2.2 | Анимация появления элементов списка (staggered)         | ☐      |
| 3.2.3 | Анимация свайпа для удаления (reveal background)        | ☐      |
| 3.2.4 | Анимация FAB (expand/collapse для выбора типа)          | ☐      |
| 3.2.5 | Плавное раскрытие карточки при нажатии                  | ☐      |
| 3.2.6 | Shimmer-эффект при загрузке                             | ☐      |
| 3.2.7 | Ripple-эффект при нажатии                               | ☐      |
| 3.2.8 | Lottie-анимации для empty states                        | ☐      |
| 3.2.9 | Анимация появления маркеров на карте                    | ☐      |

### 3.3 Экраны приложения

| №     | Экран           | Описание                           | Статус |
|-------|-----------------|-----------------------------------|--------|
| 3.3.1 | Splash Screen   | Экран загрузки с логотипом        | ☐      |
| 3.3.2 | Onboarding      | Приветственные слайды             | ☐      |
| 3.3.3 | Home (Главный)  | Лента записей с фильтрами         | ☐      |
| 3.3.4 | Add Catch       | Добавление улова/трофея           | ☐      |
| 3.3.5 | Catch Detail    | Детальный просмотр записи         | ☐      |
| 3.3.6 | Edit Catch      | Редактирование записи             | ☐      |
| 3.3.7 | Gallery         | Галерея фотографий                | ☐      |
| 3.3.8 | Photo Viewer    | Полноэкранный просмотр фото       | ☐      |
| 3.3.9 | Map             | Карта с метками мест              | ☐      |
| 3.3.10| Locations       | Список сохранённых мест           | ☐      |
| 3.3.11| Add/Edit Location | Добавление/редактирование места | ☐      |
| 3.3.12| Equipment       | Каталог снаряжения                | ☐      |
| 3.3.13| Add/Edit Equipment | Добавление/редактирование снаряжения | ☐ |
| 3.3.14| Statistics      | Статистика и графики              | ☐      |
| 3.3.15| Settings        | Настройки приложения              | ☐      |

### 3.4 Навигация

| №     | Требование                                              | Статус |
|-------|--------------------------------------------------------|--------|
| 3.4.1 | Bottom Navigation Bar (Главная, Карта, Галерея, Статистика, Ещё) | ☐ |
| 3.4.2 | Expandable FAB для добавления (Рыбалка / Охота)         | ☐      |
| 3.4.3 | Top App Bar с поиском и фильтрами                       | ☐      |
| 3.4.4 | Back navigation (системная кнопка)                      | ☐      |
| 3.4.5 | Боковое меню для доступа к настройкам и снаряжению      | ☐      |

### 3.5 UX элементы

| №     | Требование                                              | Статус |
|-------|--------------------------------------------------------|--------|
| 3.5.1 | Pull-to-refresh на главном экране                       | ☐      |
| 3.5.2 | Empty state — заглушка при пустом списке                | ☐      |
| 3.5.3 | Snackbar с Undo при удалении записи                     | ☐      |
| 3.5.4 | Bottom Sheet для фильтров                               | ☐      |
| 3.5.5 | Date Picker (Material 3)                                | ☐      |
| 3.5.6 | Time Picker (Material 3)                                | ☐      |
| 3.5.7 | Chips для видов и фильтров                              | ☐      |
| 3.5.8 | Поддержка landscape ориентации                          | ☐      |
| 3.5.9 | Edge-to-edge дизайн                                     | ☐      |
| 3.5.10| Haptic feedback при действиях                           | ☐      |
| 3.5.11| Autocomplete при вводе вида рыбы/дичи                   | ☐      |

---

## 4. Архитектурные требования

| №    | Требование                                              | Статус |
|------|--------------------------------------------------------|--------|
| 4.1  | Clean Architecture (3 слоя)                             | ☐      |
| 4.2  | Data Layer: Repository, DAO, Entity, DataSource         | ☐      |
| 4.3  | Domain Layer: UseCases, Domain Models                   | ☐      |
| 4.4  | Presentation Layer: ViewModel, UI State, Compose UI     | ☐      |
| 4.5  | Однонаправленный поток данных (UDF)                     | ☐      |
| 4.6  | Kotlin Coroutines для асинхронности                     | ☐      |
| 4.7  | Kotlin Flow для реактивных потоков                      | ☐      |
| 4.8  | StateFlow / SharedFlow в ViewModel                      | ☐      |
| 4.9  | DataStore Preferences для настроек                      | ☐      |
| 4.10 | Single Activity архитектура                             | ☐      |

---

## 5. Модель данных

### 5.1 Catch (Улов/Трофей)

```kotlin
@Entity(tableName = "catches")
data class CatchEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    // Тип активности
    @ColumnInfo(name = "activity_type")
    val activityType: ActivityType, // FISHING или HUNTING

    // Информация о добыче
    val species: String,            // Вид рыбы/дичи
    val weight: Double? = null,     // Вес в кг
    val length: Double? = null,     // Длина в см (для рыбы)
    val quantity: Int = 1,          // Количество (для охоты)

    // Связи
    @ColumnInfo(name = "location_id")
    val locationId: Long? = null,

    @ColumnInfo(name = "weather_id")
    val weatherId: Long? = null,

    // Дополнительно
    val notes: String? = null,

    // Время
    @ColumnInfo(name = "catch_date")
    val catchDate: LocalDate,

    @ColumnInfo(name = "catch_time")
    val catchTime: LocalTime? = null,

    @ColumnInfo(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @ColumnInfo(name = "updated_at")
    val updatedAt: LocalDateTime = LocalDateTime.now()
)

enum class ActivityType { FISHING, HUNTING }
```

### 5.2 Location (Место)

```kotlin
@Entity(tableName = "locations")
data class LocationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val name: String,
    val description: String? = null,

    @ColumnInfo(name = "location_type")
    val locationType: LocationType,

    val latitude: Double,
    val longitude: Double,

    @ColumnInfo(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now()
)

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
```

### 5.3 Photo (Фотография)

```kotlin
@Entity(tableName = "photos")
data class PhotoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "catch_id")
    val catchId: Long,

    @ColumnInfo(name = "file_path")
    val filePath: String,           // Путь к файлу

    @ColumnInfo(name = "is_primary")
    val isPrimary: Boolean = false, // Главное фото записи

    @ColumnInfo(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now()
)
```

### 5.4 Weather (Погодные условия)

```kotlin
@Entity(tableName = "weather")
data class WeatherEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val temperature: Int? = null,           // °C
    val pressure: Int? = null,              // мм рт. ст.
    val humidity: Int? = null,              // %

    @ColumnInfo(name = "wind_speed")
    val windSpeed: Double? = null,          // м/с

    @ColumnInfo(name = "wind_direction")
    val windDirection: WindDirection? = null,

    val cloudiness: Cloudiness? = null,
    val precipitation: Precipitation? = null,

    @ColumnInfo(name = "moon_phase")
    val moonPhase: MoonPhase? = null
)

enum class WindDirection { N, NE, E, SE, S, SW, W, NW }

enum class Cloudiness {
    CLEAR,          // Ясно
    PARTLY_CLOUDY,  // Переменная облачность
    CLOUDY,         // Облачно
    OVERCAST        // Пасмурно
}

enum class Precipitation {
    NONE,           // Без осадков
    LIGHT_RAIN,     // Небольшой дождь
    RAIN,           // Дождь
    HEAVY_RAIN,     // Сильный дождь
    SNOW            // Снег
}

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
```

### 5.5 Equipment (Снаряжение)

```kotlin
@Entity(tableName = "equipment")
data class EquipmentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val name: String,
    val description: String? = null,

    @ColumnInfo(name = "equipment_type")
    val equipmentType: EquipmentType,

    @ColumnInfo(name = "activity_type")
    val activityType: ActivityType,     // Для рыбалки или охоты

    @ColumnInfo(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now()
)

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
```

### 5.6 CatchEquipmentCrossRef (Связь улова и снаряжения)

```kotlin
@Entity(
    tableName = "catch_equipment",
    primaryKeys = ["catch_id", "equipment_id"],
    foreignKeys = [
        ForeignKey(
            entity = CatchEntity::class,
            parentColumns = ["id"],
            childColumns = ["catch_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = EquipmentEntity::class,
            parentColumns = ["id"],
            childColumns = ["equipment_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class CatchEquipmentCrossRef(
    @ColumnInfo(name = "catch_id")
    val catchId: Long,

    @ColumnInfo(name = "equipment_id")
    val equipmentId: Long
)
```

### 5.7 Связи между таблицами

```
┌─────────────┐       ┌─────────────────┐
│  Location   │       │      Catch      │
├─────────────┤       ├─────────────────┤
│ id (PK)     │◄──────│ locationId (FK) │
│ ...         │       │ weatherId (FK)  │───────┐
└─────────────┘       └────────┬────────┘       │
                               │                │
                               │ 1:N            │
                               ▼                ▼
                      ┌─────────────────┐  ┌─────────────┐
                      │     Photo       │  │   Weather   │
                      ├─────────────────┤  ├─────────────┤
                      │ catchId (FK)    │  │ id (PK)     │
                      │ ...             │  │ ...         │
                      └─────────────────┘  └─────────────┘

┌─────────────────────┐
│ CatchEquipmentCrossRef │
├─────────────────────┤
│ catchId (FK)        │───────► Catch
│ equipmentId (FK)    │───────► Equipment
└─────────────────────┘
```

---

## 6. Структура проекта

```
app/
├── src/main/
│   ├── java/com/example/trophy/
│   │   ├── di/                          # Hilt modules
│   │   │   ├── AppModule.kt
│   │   │   ├── DatabaseModule.kt
│   │   │   ├── RepositoryModule.kt
│   │   │   └── LocationModule.kt
│   │   │
│   │   ├── data/                        # Data Layer
│   │   │   ├── local/
│   │   │   │   ├── database/
│   │   │   │   │   ├── AppDatabase.kt
│   │   │   │   │   ├── dao/
│   │   │   │   │   │   ├── CatchDao.kt
│   │   │   │   │   │   ├── LocationDao.kt
│   │   │   │   │   │   ├── PhotoDao.kt
│   │   │   │   │   │   ├── WeatherDao.kt
│   │   │   │   │   │   └── EquipmentDao.kt
│   │   │   │   │   ├── entity/
│   │   │   │   │   │   ├── CatchEntity.kt
│   │   │   │   │   │   ├── LocationEntity.kt
│   │   │   │   │   │   ├── PhotoEntity.kt
│   │   │   │   │   │   ├── WeatherEntity.kt
│   │   │   │   │   │   ├── EquipmentEntity.kt
│   │   │   │   │   │   └── CatchEquipmentCrossRef.kt
│   │   │   │   │   └── converter/
│   │   │   │   │       └── Converters.kt
│   │   │   │   └── datastore/
│   │   │   │       └── SettingsDataStore.kt
│   │   │   ├── repository/
│   │   │   │   ├── CatchRepositoryImpl.kt
│   │   │   │   ├── LocationRepositoryImpl.kt
│   │   │   │   ├── PhotoRepositoryImpl.kt
│   │   │   │   ├── WeatherRepositoryImpl.kt
│   │   │   │   ├── EquipmentRepositoryImpl.kt
│   │   │   │   └── SettingsRepositoryImpl.kt
│   │   │   └── mapper/
│   │   │       ├── CatchMapper.kt
│   │   │       ├── LocationMapper.kt
│   │   │       ├── WeatherMapper.kt
│   │   │       └── EquipmentMapper.kt
│   │   │
│   │   ├── domain/                      # Domain Layer
│   │   │   ├── model/
│   │   │   │   ├── Catch.kt
│   │   │   │   ├── Location.kt
│   │   │   │   ├── Photo.kt
│   │   │   │   ├── Weather.kt
│   │   │   │   ├── Equipment.kt
│   │   │   │   ├── ActivityType.kt
│   │   │   │   ├── Species.kt
│   │   │   │   ├── LocationType.kt
│   │   │   │   ├── EquipmentType.kt
│   │   │   │   ├── WeatherEnums.kt
│   │   │   │   └── TrophyStatistics.kt
│   │   │   ├── repository/
│   │   │   │   ├── CatchRepository.kt
│   │   │   │   ├── LocationRepository.kt
│   │   │   │   ├── PhotoRepository.kt
│   │   │   │   ├── WeatherRepository.kt
│   │   │   │   ├── EquipmentRepository.kt
│   │   │   │   └── SettingsRepository.kt
│   │   │   └── usecase/
│   │   │       ├── catch/
│   │   │       │   ├── GetCatchesUseCase.kt
│   │   │       │   ├── GetCatchByIdUseCase.kt
│   │   │       │   ├── AddCatchUseCase.kt
│   │   │       │   ├── UpdateCatchUseCase.kt
│   │   │       │   ├── DeleteCatchUseCase.kt
│   │   │       │   └── SearchCatchesUseCase.kt
│   │   │       ├── location/
│   │   │       │   ├── GetLocationsUseCase.kt
│   │   │       │   ├── AddLocationUseCase.kt
│   │   │       │   ├── GetCurrentLocationUseCase.kt
│   │   │       │   └── DeleteLocationUseCase.kt
│   │   │       ├── photo/
│   │   │       │   ├── GetPhotosUseCase.kt
│   │   │       │   ├── AddPhotoUseCase.kt
│   │   │       │   ├── DeletePhotoUseCase.kt
│   │   │       │   └── SetPrimaryPhotoUseCase.kt
│   │   │       ├── equipment/
│   │   │       │   ├── GetEquipmentUseCase.kt
│   │   │       │   ├── AddEquipmentUseCase.kt
│   │   │       │   ├── UpdateEquipmentUseCase.kt
│   │   │       │   └── DeleteEquipmentUseCase.kt
│   │   │       ├── statistics/
│   │   │       │   ├── GetStatisticsUseCase.kt
│   │   │       │   ├── GetCatchesByPeriodUseCase.kt
│   │   │       │   ├── GetTopSpeciesUseCase.kt
│   │   │       │   ├── GetTopLocationsUseCase.kt
│   │   │       │   └── GetWeatherAnalysisUseCase.kt
│   │   │       └── export/
│   │   │           ├── ExportDataUseCase.kt
│   │   │           └── ImportDataUseCase.kt
│   │   │
│   │   ├── presentation/                # Presentation Layer
│   │   │   ├── MainActivity.kt
│   │   │   ├── navigation/
│   │   │   │   ├── NavGraph.kt
│   │   │   │   └── Screen.kt
│   │   │   ├── theme/
│   │   │   │   ├── Theme.kt
│   │   │   │   ├── Color.kt
│   │   │   │   └── Type.kt
│   │   │   ├── components/              # Reusable UI components
│   │   │   │   ├── CatchCard.kt
│   │   │   │   ├── SpeciesChip.kt
│   │   │   │   ├── WeatherDisplay.kt
│   │   │   │   ├── LocationBadge.kt
│   │   │   │   ├── PhotoThumbnail.kt
│   │   │   │   ├── PhotoGallery.kt
│   │   │   │   ├── EmptyState.kt
│   │   │   │   ├── SearchBar.kt
│   │   │   │   ├── FilterChips.kt
│   │   │   │   ├── ExpandableFab.kt
│   │   │   │   ├── WeatherInputForm.kt
│   │   │   │   └── EquipmentSelector.kt
│   │   │   └── screens/
│   │   │       ├── home/
│   │   │       │   ├── HomeScreen.kt
│   │   │       │   ├── HomeViewModel.kt
│   │   │       │   └── HomeUiState.kt
│   │   │       ├── catch_detail/
│   │   │       │   ├── CatchDetailScreen.kt
│   │   │       │   ├── CatchDetailViewModel.kt
│   │   │       │   └── CatchDetailUiState.kt
│   │   │       ├── add_catch/
│   │   │       │   ├── AddCatchScreen.kt
│   │   │       │   ├── AddCatchViewModel.kt
│   │   │       │   └── AddCatchUiState.kt
│   │   │       ├── edit_catch/
│   │   │       │   ├── EditCatchScreen.kt
│   │   │       │   ├── EditCatchViewModel.kt
│   │   │       │   └── EditCatchUiState.kt
│   │   │       ├── gallery/
│   │   │       │   ├── GalleryScreen.kt
│   │   │       │   ├── GalleryViewModel.kt
│   │   │       │   └── GalleryUiState.kt
│   │   │       ├── photo_viewer/
│   │   │       │   └── PhotoViewerScreen.kt
│   │   │       ├── map/
│   │   │       │   ├── MapScreen.kt
│   │   │       │   ├── MapViewModel.kt
│   │   │       │   └── MapUiState.kt
│   │   │       ├── locations/
│   │   │       │   ├── LocationsScreen.kt
│   │   │       │   ├── LocationsViewModel.kt
│   │   │       │   └── LocationsUiState.kt
│   │   │       ├── add_location/
│   │   │       │   ├── AddLocationScreen.kt
│   │   │       │   ├── AddLocationViewModel.kt
│   │   │       │   └── AddLocationUiState.kt
│   │   │       ├── equipment/
│   │   │       │   ├── EquipmentScreen.kt
│   │   │       │   ├── EquipmentViewModel.kt
│   │   │       │   └── EquipmentUiState.kt
│   │   │       ├── add_equipment/
│   │   │       │   ├── AddEquipmentScreen.kt
│   │   │       │   ├── AddEquipmentViewModel.kt
│   │   │       │   └── AddEquipmentUiState.kt
│   │   │       ├── statistics/
│   │   │       │   ├── StatisticsScreen.kt
│   │   │       │   ├── StatisticsViewModel.kt
│   │   │       │   └── StatisticsUiState.kt
│   │   │       ├── settings/
│   │   │       │   ├── SettingsScreen.kt
│   │   │       │   ├── SettingsViewModel.kt
│   │   │       │   └── SettingsUiState.kt
│   │   │       └── onboarding/
│   │   │           ├── OnboardingScreen.kt
│   │   │           └── OnboardingViewModel.kt
│   │   │
│   │   ├── service/                     # Background services
│   │   │   └── LocationService.kt
│   │   │
│   │   └── util/                        # Utilities
│   │       ├── DateTimeUtils.kt
│   │       ├── LocationUtils.kt
│   │       ├── PhotoUtils.kt
│   │       ├── WeatherUtils.kt
│   │       └── JsonUtils.kt
│   │
│   ├── res/
│   │   ├── values/
│   │   │   ├── strings.xml
│   │   │   ├── colors.xml
│   │   │   └── themes.xml
│   │   └── raw/
│   │       └── *.json (lottie animations)
│   │
│   └── AndroidManifest.xml
│
├── build.gradle.kts (app)
└── build.gradle.kts (project)
```

---

## 7. Технологии и библиотеки

| Категория      | Библиотека                    | Версия       |
|----------------|-------------------------------|--------------|
| **UI**         | Jetpack Compose BOM           | 2024.12.01   |
|                | Material 3                    | 1.3.x        |
|                | Compose Navigation            | 2.8.x        |
|                | Compose Animation             | (BOM)        |
| **DI**         | Hilt                          | 2.52         |
|                | Hilt Navigation Compose       | 1.2.x        |
| **Database**   | Room                          | 2.6.x        |
| **Async**      | Coroutines                    | 1.9.x        |
| **Settings**   | DataStore Preferences         | 1.1.x        |
| **Date/Time**  | Core Library Desugaring       | 2.1.x        |
| **Charts**     | Vico                          | 2.x          |
| **Animations** | Lottie Compose                | 6.x          |
| **JSON**       | Kotlinx Serialization         | 1.7.x        |
| **Maps**       | OSMDroid                      | 6.x          |
| **Location**   | Google Play Services Location | 21.x         |
| **Images**     | Coil                          | 2.x          |
| **Camera**     | CameraX                       | 1.4.x        |

---

## 8. Настройки приложения

| №   | Настройка                               | Тип      | По умолчанию |
|-----|-----------------------------------------|----------|--------------|
| 8.1 | Тема (светлая/тёмная/системная)         | Enum     | Системная    |
| 8.2 | Dynamic Color                           | Boolean  | true         |
| 8.3 | Единицы веса (кг/фунты)                 | Enum     | кг           |
| 8.4 | Единицы длины (см/дюймы)                | Enum     | см           |
| 8.5 | Единицы давления (мм рт.ст./гПа)        | Enum     | мм рт.ст.    |
| 8.6 | Сортировка по умолчанию                 | Enum     | По дате      |
| 8.7 | Качество сжатия фото (%)                | Int      | 80           |
| 8.8 | Онбординг показан                       | Boolean  | false        |
| 8.9 | Последний тип активности                | Enum     | FISHING      |

---

## 9. Нефункциональные требования

| №   | Требование                                           | Статус |
|-----|-----------------------------------------------------|--------|
| 9.1 | Время холодного запуска < 2 сек                     | ☐      |
| 9.2 | Плавность анимаций 60 fps                           | ☐      |
| 9.3 | Offline-first (работа без интернета)                | ☐      |
| 9.4 | Поддержка русского языка                            | ☐      |
| 9.5 | Обработка ошибок с понятными сообщениями            | ☐      |
| 9.6 | Graceful degradation на старых устройствах          | ☐      |
| 9.7 | Оптимизация размера фотографий                      | ☐      |
| 9.8 | Корректная работа с разрешениями (камера, геолокация) | ☐   |

---

## 10. Этапы разработки (Roadmap)

### Этап 1: Базовая структура

- [ ] Создание проекта, настройка Gradle
- [ ] Настройка Hilt
- [ ] Создание Room Database и Entity
- [ ] Базовая тема Material 3

### Этап 2: CRUD записей

- [ ] CatchDao, CatchRepository
- [ ] Home Screen с лентой записей
- [ ] Экран добавления записи (рыба/дичь)
- [ ] Экран детального просмотра
- [ ] Редактирование и удаление

### Этап 3: Геолокация и места

- [ ] LocationDao, LocationRepository
- [ ] Получение текущего местоположения
- [ ] Список сохранённых мест
- [ ] Интеграция OSMDroid для карты
- [ ] Привязка места к записи

### Этап 4: Фотографии

- [ ] PhotoDao, PhotoRepository
- [ ] Интеграция CameraX
- [ ] Выбор фото из галереи (Coil)
- [ ] Галерея трофеев
- [ ] Полноэкранный просмотр

### Этап 5: Погода и снаряжение

- [ ] WeatherEntity, WeatherDao
- [ ] Форма ввода погодных условий
- [ ] EquipmentEntity, EquipmentDao
- [ ] Каталог снаряжения
- [ ] Привязка снаряжения к записи

### Этап 6: Статистика

- [ ] Экран статистики
- [ ] Графики (Vico)
- [ ] Расчёт метрик и рекордов
- [ ] Анализ по местам и снаряжению

### Этап 7: Фильтрация и поиск

- [ ] Фильтры по типу, виду, периоду
- [ ] Комбинированные фильтры
- [ ] Поиск по названию
- [ ] Сортировка

### Этап 8: Экспорт/Импорт

- [ ] JSON сериализация
- [ ] SAF для выбора файла
- [ ] Экспорт/импорт фотографий

### Этап 9: Онбординг и полировка

- [ ] Онбординг экраны
- [ ] Запрос разрешений
- [ ] Анимации и переходы
- [ ] Тестирование

---

## 11. Предустановленные данные

### 11.1 Виды рыб

```kotlin
val defaultFishSpecies = listOf(
    // Хищные
    "Щука", "Окунь", "Судак", "Сом", "Жерех", "Голавль", "Язь",
    // Мирные
    "Карп", "Карась", "Лещ", "Плотва", "Линь", "Густера", "Белый амур",
    // Лососевые
    "Форель", "Хариус", "Таймень", "Сиг", "Нельма",
    // Другие
    "Налим", "Угорь", "Сазан", "Толстолобик"
)
```

### 11.2 Виды дичи

```kotlin
val defaultGameSpecies = listOf(
    // Копытные
    "Лось", "Кабан", "Косуля", "Олень", "Марал",
    // Пернатая дичь
    "Утка", "Гусь", "Фазан", "Вальдшнеп", "Тетерев", "Глухарь", "Рябчик", "Куропатка", "Перепел",
    // Пушная дичь
    "Заяц", "Лиса", "Бобр", "Енот", "Барсук", "Ондатра"
)
```

---

**Дата создания:** 2026-01-10
**Версия:** 1.0
