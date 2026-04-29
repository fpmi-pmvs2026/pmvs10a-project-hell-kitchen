package com.mealhunter.app.ui.screens.fridge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mealhunter.app.data.db.MyIngredientEntity
import com.mealhunter.app.data.model.Recipe
import com.mealhunter.app.data.model.RecipeFilter
import com.mealhunter.app.data.repository.RecipeRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class FridgeUiState(
    val ingredients: List<MyIngredientEntity> = emptyList(),
    val newIngredient: String = "",
    val recipes: List<Recipe> = emptyList(),
    val isSearching: Boolean = false,
    val hasSearched: Boolean = false,
    val error: String? = null
)

class FridgeViewModel(private val repository: RecipeRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(FridgeUiState())
    val uiState: StateFlow<FridgeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getMyIngredients().collect { ingredients ->
                _uiState.value = _uiState.value.copy(ingredients = ingredients)
            }
        }
    }

    fun onNewIngredientChange(value: String) {
        _uiState.value = _uiState.value.copy(newIngredient = value)
    }

    fun addIngredient() {
        val name = _uiState.value.newIngredient.trim()
        if (name.isEmpty()) return
        viewModelScope.launch {
            repository.addIngredient(name)
            _uiState.value = _uiState.value.copy(newIngredient = "")
        }
    }

    fun removeIngredient(id: Int) {
        viewModelScope.launch {
            repository.removeIngredient(id)
        }
    }

    fun searchByIngredients() {
        val ingredients = _uiState.value.ingredients
        if (ingredients.isEmpty()) return

        val ingredientString = ingredients.joinToString(",") { it.name }
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSearching = true, error = null, hasSearched = true)
            val result = repository.searchRecipes(
                RecipeFilter(includeIngredients = ingredientString)
            )
            result.onSuccess { recipes ->
                _uiState.value = _uiState.value.copy(
                    recipes = recipes,
                    isSearching = false
                )
            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(
                    isSearching = false,
                    error = e.message ?: "Search failed"
                )
            }
        }
    }

    fun toggleFavorite(recipe: Recipe) {
        viewModelScope.launch {
            val newState = repository.toggleFavorite(recipe)
            _uiState.value = _uiState.value.copy(
                recipes = _uiState.value.recipes.map {
                    if (it.id == recipe.id) it.copy(isFavorite = newState) else it
                }
            )
        }
    }

    fun clearIngredients() {
        viewModelScope.launch {
            repository.clearIngredients()
        }
    }
}
