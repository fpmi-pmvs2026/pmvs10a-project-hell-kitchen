package com.mealhunter.app.ui.screens.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mealhunter.app.data.model.Recipe
import com.mealhunter.app.data.repository.RecipeRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class FavoritesUiState(
    val favorites: List<Recipe> = emptyList(),
    val isLoading: Boolean = true
)

class FavoritesViewModel(private val repository: RecipeRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getFavorites().collect { recipes ->
                _uiState.value = FavoritesUiState(
                    favorites = recipes,
                    isLoading = false
                )
            }
        }
    }

    fun removeFavorite(recipe: Recipe) {
        viewModelScope.launch {
            repository.toggleFavorite(recipe)
        }
    }
}
