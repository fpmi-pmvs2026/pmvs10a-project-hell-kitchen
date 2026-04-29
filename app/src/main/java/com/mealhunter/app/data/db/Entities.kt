package com.mealhunter.app.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_recipes")
data class FavoriteRecipeEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val image: String?,
    val readyInMinutes: Int,
    val servings: Int,
    val summary: String,
    val cuisines: String, // JSON string
    val dishTypes: String, // JSON string
    val diets: String, // JSON string
    val ingredients: String, // JSON string
    val steps: String, // JSON string
    val vegetarian: Boolean,
    val vegan: Boolean,
    val glutenFree: Boolean,
    val dairyFree: Boolean,
    val healthScore: Double,
    val addedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "search_history")
data class SearchHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val query: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "my_ingredients")
data class MyIngredientEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val addedAt: Long = System.currentTimeMillis()
)
