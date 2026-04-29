package com.mealhunter.app.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mealhunter.app.data.db.SearchHistoryEntity
import com.mealhunter.app.data.model.Recipe
import com.mealhunter.app.data.model.RecipeFilter
import com.mealhunter.app.data.repository.RecipeRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class SearchUiState(
    val query: String = "",
    val results: List<Recipe> = emptyList(),
    val searchHistory: List<SearchHistoryEntity> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val hasSearched: Boolean = false
)

class SearchViewModel(private val repository: RecipeRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getSearchHistory().collect { history ->
                _uiState.value = _uiState.value.copy(searchHistory = history)
            }
        }
    }

    fun onQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(query = query)
    }

    fun search() {
        val query = _uiState.value.query.trim()
        if (query.isEmpty()) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null, hasSearched = true)
            repository.addSearchQuery(query)
            val result = repository.searchRecipes(RecipeFilter(query = query))
            result.onSuccess { recipes ->
                _uiState.value = _uiState.value.copy(
                    results = recipes,
                    isLoading = false
                )
            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Search failed"
                )
            }
        }
    }

    fun searchFromHistory(query: String) {
        _uiState.value = _uiState.value.copy(query = query)
        search()
    }

    fun toggleFavorite(recipe: Recipe) {
        viewModelScope.launch {
            val newFavState = repository.toggleFavorite(recipe)
            _uiState.value = _uiState.value.copy(
                results = _uiState.value.results.map {
                    if (it.id == recipe.id) it.copy(isFavorite = newFavState) else it
                }
            )
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            repository.clearSearchHistory()
        }
    }
}
