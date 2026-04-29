package com.mealhunter.app.di

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mealhunter.app.BuildConfig
import com.mealhunter.app.data.api.RetrofitClient
import com.mealhunter.app.data.db.MealHunterDatabase
import com.mealhunter.app.data.repository.RecipeRepository
import com.mealhunter.app.ui.screens.detail.DetailViewModel
import com.mealhunter.app.ui.screens.favorites.FavoritesViewModel
import com.mealhunter.app.ui.screens.fridge.FridgeViewModel
import com.mealhunter.app.ui.screens.home.HomeViewModel
import com.mealhunter.app.ui.screens.search.SearchViewModel

class ViewModelFactory(context: Context) : ViewModelProvider.Factory {

    private val database = MealHunterDatabase.getInstance(context)
    private val repository = RecipeRepository(
        api = RetrofitClient.api,
        favoriteDao = database.favoriteRecipeDao(),
        searchHistoryDao = database.searchHistoryDao(),
        ingredientDao = database.myIngredientDao(),
        apiKey = BuildConfig.SPOONACULAR_API_KEY
    )

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) ->
                HomeViewModel(repository) as T
            modelClass.isAssignableFrom(SearchViewModel::class.java) ->
                SearchViewModel(repository) as T
            modelClass.isAssignableFrom(FavoritesViewModel::class.java) ->
                FavoritesViewModel(repository) as T
            modelClass.isAssignableFrom(FridgeViewModel::class.java) ->
                FridgeViewModel(repository) as T
            modelClass.isAssignableFrom(DetailViewModel::class.java) ->
                DetailViewModel(repository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
