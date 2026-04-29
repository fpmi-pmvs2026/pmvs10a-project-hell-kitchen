package com.mealhunter.app.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteRecipeDao {
    @Query("SELECT * FROM favorite_recipes ORDER BY addedAt DESC")
    fun getAllFavorites(): Flow<List<FavoriteRecipeEntity>>

    @Query("SELECT id FROM favorite_recipes")
    suspend fun getAllFavoriteIds(): List<Int>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_recipes WHERE id = :recipeId)")
    suspend fun isFavorite(recipeId: Int): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recipe: FavoriteRecipeEntity)

    @Query("DELETE FROM favorite_recipes WHERE id = :recipeId")
    suspend fun deleteById(recipeId: Int)

    @Query("DELETE FROM favorite_recipes")
    suspend fun deleteAll()
}

@Dao
interface SearchHistoryDao {
    @Query("SELECT * FROM search_history ORDER BY timestamp DESC LIMIT 20")
    fun getRecentSearches(): Flow<List<SearchHistoryEntity>>

    @Insert
    suspend fun insert(search: SearchHistoryEntity)

    @Query("DELETE FROM search_history")
    suspend fun deleteAll()
}

@Dao
interface MyIngredientDao {
    @Query("SELECT * FROM my_ingredients ORDER BY name ASC")
    fun getAllIngredients(): Flow<List<MyIngredientEntity>>

    @Insert
    suspend fun insert(ingredient: MyIngredientEntity)

    @Query("DELETE FROM my_ingredients WHERE id = :ingredientId")
    suspend fun deleteById(ingredientId: Int)

    @Query("DELETE FROM my_ingredients")
    suspend fun deleteAll()
}
