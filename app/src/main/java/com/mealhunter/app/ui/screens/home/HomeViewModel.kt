package com.mealhunter.app.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mealhunter.app.data.model.Recipe
import com.mealhunter.app.data.model.RecipeFilter
import com.mealhunter.app.data.repository.RecipeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HomeUiState(
    val recipes: List<Recipe> = emptyList(),
    val currentIndex: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null,
    val filter: RecipeFilter = RecipeFilter()
)

class HomeViewModel(private val repository: RecipeRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadRecipes()
    }

    fun loadRecipes() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val filter = _uiState.value.filter
            val result = if (filter == RecipeFilter()) {
                repository.getRandomRecipes()
            } else {
                repository.searchRecipes(filter)
            }
            result.onSuccess { recipes ->
                _uiState.value = _uiState.value.copy(
                    recipes = recipes,
                    currentIndex = 0,
                    isLoading = false
                )
            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load recipes"
                )
            }
        }
    }

    fun onSwipeRight(recipe: Recipe) {
        viewModelScope.launch {
            repository.toggleFavorite(recipe)
            moveToNext()
        }
    }

    fun onSwipeLeft() {
        moveToNext()
    }

    private fun moveToNext() {
        val state = _uiState.value
        val nextIndex = state.currentIndex + 1
        if (nextIndex >= state.recipes.size) {
            // Load more recipes
            loadRecipes()
        } else {
            _uiState.value = state.copy(currentIndex = nextIndex)
        }
    }

    fun updateFilter(filter: RecipeFilter) {
        _uiState.value = _uiState.value.copy(filter = filter)
        loadRecipes()
    }

    fun getCurrentRecipe(): Recipe? {
        val state = _uiState.value
        return state.recipes.getOrNull(state.currentIndex)
    }
}
