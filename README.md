# MealHunter 🍽️

## Description
MealHunter — мобильное Android-приложение для поиска и подбора рецептов в стиле "Тиндер для еды". Пользователь просматривает карточки рецептов, свайпая вправо для сохранения в избранное или влево для пропуска. Приложение поддерживает фильтрацию по кухне, диете, типу блюда, а также поиск рецептов по ингредиентам, имеющимся в холодильнике.

Приложение разработано с использованием Jetpack Compose, архитектуры MVVM, базы данных Room и Spoonacular API.

## Installation

1. Клонировать репозиторий:
```bash
git clone https://github.com/Hell-kitchen/MealHunter.git
cd MealHunter
```

2. Получить API-ключ Spoonacular:
   - Зарегистрироваться на [spoonacular.com](https://spoonacular.com/food-api)
   - Скопировать API-ключ из личного кабинета

3. Добавить ключ в `gradle.properties` (в корне проекта):
```
SPOONACULAR_API_KEY=ваш_ключ_здесь
```

4. Открыть проект в Android Studio (Hedgehog или новее)

5. Синхронизировать Gradle и запустить на эмуляторе или устройстве (minSdk 26)

## Usage

### Discover (Главный экран)
Свайпайте карточки рецептов:
- **Свайп вправо** → сохранить в избранное
- **Свайп влево** → пропустить
- **Нажатие** → открыть детальную страницу рецепта
- **Фильтры** → выбрать кухню, диету, тип блюда, максимальное время приготовления

### Search (Поиск)
Поиск рецептов по названию с сохранением истории поиска.

### My Fridge (Холодильник)
Добавьте ингредиенты, которые есть дома, и приложение найдёт рецепты, которые можно приготовить.

### Favorites (Избранное)
Список сохранённых рецептов с возможностью удаления свайпом.

## Tech Stack

| Технология | Назначение |
|---|---|
| Kotlin | Язык программирования |
| Jetpack Compose | UI-фреймворк |
| Room | Локальная база данных |
| Retrofit | HTTP-клиент для API |
| Coil | Загрузка изображений |
| Kotlin Coroutines | Многопоточность |
| Navigation Compose | Навигация между экранами |
| GitHub Actions | CI/CD |
| Spoonacular API | Данные о рецептах |

## Architecture

Приложение построено по архитектуре **MVVM** (Model-View-ViewModel):

```
app/
├── data/
│   ├── api/          # Retrofit API interface
│   ├── db/           # Room database, DAOs, entities
│   ├── model/        # Data models and mappers
│   └── repository/   # Repository pattern
├── di/               # Dependency injection (ViewModelFactory)
└── ui/
    ├── components/   # Reusable Compose components
    ├── navigation/   # NavGraph and bottom navigation
    ├── screens/      # Screen composables + ViewModels
    └── theme/        # Colors, typography, theme
```

## Database Schema

### favorite_recipes
| Column | Type | Description |
|---|---|---|
| id | INT (PK) | ID рецепта из API |
| title | TEXT | Название рецепта |
| image | TEXT | URL изображения |
| readyInMinutes | INT | Время приготовления |
| cuisines | TEXT | JSON массив кухонь |
| ingredients | TEXT | JSON массив ингредиентов |
| steps | TEXT | JSON массив шагов |
| addedAt | LONG | Timestamp добавления |

### search_history
| Column | Type | Description |
|---|---|---|
| id | INT (PK, auto) | ID записи |
| query | TEXT | Поисковый запрос |
| timestamp | LONG | Время поиска |

### my_ingredients
| Column | Type | Description |
|---|---|---|
| id | INT (PK, auto) | ID записи |
| name | TEXT | Название ингредиента |
| addedAt | LONG | Время добавления |

## Contributing

| Участник | Роль | Задачи |
|---|---|---|
| **Таня** | Team Lead | Настройка проекта и Gradle, интеграция Spoonacular API, тема Material3, экран Discover (Home), навигация (Bottom Navigation), DI и MainActivity, README |
| **Олег** | Developer | Swipeable карточка рецепта, общие UI-компоненты, экран Search, экран деталей рецепта, CI/CD (GitHub Actions), документация и Wiki |
| **Алеся** | Developer | Room база данных, Repository pattern, экран Favorites, экран My Fridge, Unit-тесты, UI-тесты |

## License

This project is for educational purposes.
