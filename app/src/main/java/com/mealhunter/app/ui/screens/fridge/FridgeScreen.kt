package com.mealhunter.app.ui.screens.fridge

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.mealhunter.app.ui.components.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FridgeScreen(
    viewModel: FridgeViewModel,
    onRecipeClick: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Fridge") },
                actions = {
                    if (uiState.ingredients.isNotEmpty()) {
                        IconButton(onClick = { viewModel.clearIngredients() }) {
                            Icon(Icons.Default.DeleteSweep, contentDescription = "Clear all")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            // Input field
            item {
                Text(
                    "What's in your fridge?",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = uiState.newIngredient,
                        onValueChange = { viewModel.onNewIngredientChange(it) },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Add ingredient...") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { viewModel.addIngredient() }),
                        shape = MaterialTheme.shapes.large
                    )
                    FilledIconButton(
                        onClick = { viewModel.addIngredient() },
                        enabled = uiState.newIngredient.isNotBlank()
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add")
                    }
                }
            }

            // Ingredient chips
            if (uiState.ingredients.isNotEmpty()) {
                item {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        uiState.ingredients.forEach { ingredient ->
                            InputChip(
                                selected = false,
                                onClick = { viewModel.removeIngredient(ingredient.id) },
                                label = { Text(ingredient.name) },
                                trailingIcon = {
                                    Icon(
                                        Icons.Default.Close,
                                        contentDescription = "Remove",
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            )
                        }
                    }
                }

                // Search button
                item {
                    Button(
                        onClick = { viewModel.searchByIngredients() },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !uiState.isSearching
                    ) {
                        if (uiState.isSearching) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = MaterialTheme.colorScheme.onPrimary,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        Icon(Icons.Default.Search, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Find recipes with these ingredients")
                    }
                }
            }

            // Results
            when {
                uiState.error != null -> {
                    item {
                        ErrorMessage(
                            message = uiState.error!!,
                            onRetry = { viewModel.searchByIngredients() }
                        )
                    }
                }
                uiState.hasSearched && uiState.recipes.isEmpty() && !uiState.isSearching -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "No recipes found with these ingredients",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
                uiState.recipes.isNotEmpty() -> {
                    item {
                        Text(
                            "Recipes you can make",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    items(uiState.recipes, key = { it.id }) { recipe ->
                        RecipeListItem(
                            recipe = recipe,
                            onClick = { onRecipeClick(recipe.id) },
                            onFavoriteClick = { viewModel.toggleFavorite(recipe) }
                        )
                    }
                }
                !uiState.hasSearched && uiState.ingredients.isEmpty() -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 48.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("🧊", style = MaterialTheme.typography.displayLarge)
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    "Add ingredients from your fridge",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    "We'll find recipes you can make!",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
