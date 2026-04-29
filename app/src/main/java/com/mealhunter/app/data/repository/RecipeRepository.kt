package com.mealhunter.app.data.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mealhunter.app.data.api.SpoonacularApi
import com.mealhunter.app.data.db.*
import com.mealhunter.app.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RecipeRepository(
    private val api: SpoonacularApi,
    private val favoriteDao: FavoriteRecipeDao,
    private val searchHistoryDao: SearchHistoryDao,
    private val ingredientDao: MyIngredientDao,
    private val apiKey: String
) {
    private val gson = Gson()

    // --- API ---

    suspend fun searchRecipes(filter: RecipeFilter, offset: Int = 0): Result<List<Recipe>> {
        return try {
            val favoriteIds = favoriteDao.getAllFavoriteIds().toSet()
            val response = api.searchRecipes(
                apiKey = apiKey,
                query = filter.query,
                cuisine = filter.cuisine,
                diet = filter.diet,
                type = filter.type,
                maxReadyTime = filter.maxReadyTime,
                includeIngredients = filter.includeIngredients,
                offset = offset,
                number = 20
            )
            val recipes = response.results.map { it.toDomain(isFavorite = it.id in favoriteIds) }
            Result.success(recipes)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getRandomRecipes(): Result<List<Recipe>> {
        return try {
            val favoriteIds = favoriteDao.getAllFavoriteIds().toSet()
            val response = api.getRandomRecipes(apiKey = apiKey, number = 20)
            val recipes = response.recipes.map { it.toDomain(isFavorite = it.id in favoriteIds) }
            Result.success(recipes)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getRecipeDetails(recipeId: Int): Result<Recipe> {
        return try {
            val dto = api.getRecipeDetails(recipeId = recipeId, apiKey = apiKey)
            val isFav = favoriteDao.isFavorite(recipeId)
            Result.success(dto.toDomain(isFavorite = isFav))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // --- Favorites ---

    fun getFavorites(): Flow<List<Recipe>> {
        return favoriteDao.getAllFavorites().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    suspend fun toggleFavorite(recipe: Recipe): Boolean {
        val isFav = favoriteDao.isFavorite(recipe.id)
        if (isFav) {
            favoriteDao.deleteById(recipe.id)
        } else {
            favoriteDao.insert(recipe.toEntity())
        }
        return !isFav
    }

    suspend fun isFavorite(recipeId: Int): Boolean = favoriteDao.isFavorite(recipeId)

    // --- Search History ---

    fun getSearchHistory(): Flow<List<SearchHistoryEntity>> = searchHistoryDao.getRecentSearches()

    suspend fun addSearchQuery(query: String) {
        searchHistoryDao.insert(SearchHistoryEntity(query = query))
    }

    suspend fun clearSearchHistory() = searchHistoryDao.deleteAll()

    // --- My Ingredients (Fridge) ---

    fun getMyIngredients(): Flow<List<MyIngredientEntity>> = ingredientDao.getAllIngredients()

    suspend fun addIngredient(name: String) {
        ingredientDao.insert(MyIngredientEntity(name = name.trim().lowercase()))
    }

    suspend fun removeIngredient(id: Int) = ingredientDao.deleteById(id)

    suspend fun clearIngredients() = ingredientDao.deleteAll()

    // --- Mappers ---

    private fun Recipe.toEntity(): FavoriteRecipeEntity = FavoriteRecipeEntity(
        id = id,
        title = title,
        image = image,
        readyInMinutes = readyInMinutes,
        servings = servings,
        summary = summary,
        cuisines = gson.toJson(cuisines),
        dishTypes = gson.toJson(dishTypes),
        diets = gson.toJson(diets),
        ingredients = gson.toJson(ingredients),
        steps = gson.toJson(steps),
        vegetarian = vegetarian,
        vegan = vegan,
        glutenFree = glutenFree,
        dairyFree = dairyFree,
        healthScore = healthScore
    )

    private fun FavoriteRecipeEntity.toDomain(): Recipe {
        val ingredientListType = object : TypeToken<List<Ingredient>>() {}.type
        val stringListType = object : TypeToken<List<String>>() {}.type
        return Recipe(
            id = id,
            title = title,
            image = image,
            readyInMinutes = readyInMinutes,
            servings = servings,
            summary = summary,
            cuisines = gson.fromJson(cuisines, stringListType) ?: emptyList(),
            dishTypes = gson.fromJson(dishTypes, stringListType) ?: emptyList(),
            diets = gson.fromJson(diets, stringListType) ?: emptyList(),
            ingredients = gson.fromJson(ingredients, ingredientListType) ?: emptyList(),
            steps = gson.fromJson(steps, stringListType) ?: emptyList(),
            vegetarian = vegetarian,
            vegan = vegan,
            glutenFree = glutenFree,
            dairyFree = dairyFree,
            healthScore = healthScore,
            isFavorite = true
        )
    }
}
