package com.mealhunter.app.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mealhunter.app.data.model.Recipe
import com.mealhunter.app.data.repository.RecipeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class DetailUiState(
    val recipe: Recipe? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

class DetailViewModel(private val repository: RecipeRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    fun loadRecipe(recipeId: Int) {
        viewModelScope.launch {
            _uiState.value = DetailUiState(isLoading = true)
            val result = repository.getRecipeDetails(recipeId)
            result.onSuccess { recipe ->
                _uiState.value = DetailUiState(recipe = recipe)
            }.onFailure { e ->
                _uiState.value = DetailUiState(error = e.message ?: "Failed to load recipe")
            }
        }
    }

    fun toggleFavorite() {
        val recipe = _uiState.value.recipe ?: return
        viewModelScope.launch {
            val newState = repository.toggleFavorite(recipe)
            _uiState.value = _uiState.value.copy(
                recipe = recipe.copy(isFavorite = newState)
            )
        }
    }
}
