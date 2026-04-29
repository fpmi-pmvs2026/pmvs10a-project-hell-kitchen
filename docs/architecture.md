# Архитектура

MealHunter построен по паттерну **MVVM** (Model-View-ViewModel) с Repository pattern.

## Схема слоёв

```
┌─────────────────────────────────────────────┐
│                Слой представления           │
│   Composable-экраны + ViewModel-классы      │
│   Home · Search · Detail · Fav · Fridge     │
└────────────────────┬────────────────────────┘
                     │ StateFlow / collectAsState
┌────────────────────▼────────────────────────┐
│              Слой данных (Repository)       │
│           RecipeRepository                  │
│  searchRecipes · getFavorites · toggleFav   │
└─────────────┬───────────────────┬───────────┘
              │                   │
┌─────────────▼──────┐ ┌──────────▼──────────┐
│  Удалённый источник│ │  Локальный источник  │
│  SpoonacularApi    │ │  База данных Room    │
│  (Retrofit/OkHttp) │ │  favorite_recipes   │
│                    │ │  search_history     │
│                    │ │  my_ingredients     │
└────────────────────┘ └─────────────────────┘
```

## Структура файлов

```
com.mealhunter.app
├── data
│   ├── api
│   │   ├── SpoonacularApi.kt      ← Retrofit-интерфейс
│   │   └── RetrofitClient.kt      ← OkHttp-синглтон
│   ├── db
│   │   ├── Entities.kt            ← Room-сущности
│   │   ├── Daos.kt                ← DAO с поддержкой Flow
│   │   └── MealHunterDatabase.kt  ← синглтон базы данных
│   ├── model
│   │   └── Models.kt              ← DTO, доменные модели, маппинг
│   └── repository
│       └── RecipeRepository.kt    ← единый источник истины
├── di
│   └── ViewModelFactory.kt        ← ручной DI
├── ui
│   ├── components
│   │   ├── SwipeableRecipeCard.kt ← жесты свайпа + анимация
│   │   └── SharedComponents.kt   ← RecipeListItem, бейджи, лоадеры
│   ├── navigation
│   │   └── Navigation.kt         ← NavHost + нижняя навигация
│   ├── screens
│   │   ├── home/                 ← HomeScreen + HomeViewModel
│   │   ├── search/               ← SearchScreen + SearchViewModel
│   │   ├── detail/               ← DetailScreen + DetailViewModel
│   │   ├── favorites/            ← FavoritesScreen + FavoritesViewModel
│   │   └── fridge/               ← FridgeScreen + FridgeViewModel
│   └── theme
│       └── Theme.kt
└── MainActivity.kt
```

## Ключевые архитектурные решения

- **Одна Activity** — `MainActivity` содержит все экраны через `NavHost`
- **Ручной DI** — `ViewModelFactory` вместо Hilt для упрощения проекта
- **Единый UiState** — каждый ViewModel публикует один `StateFlow<UiState>`
- **Room + Flow** — реактивные обновления избранного и ингредиентов без поллинга
- **Gson для списков** — поля-коллекции хранятся как JSON-строки, чтобы не создавать лишние таблицы
