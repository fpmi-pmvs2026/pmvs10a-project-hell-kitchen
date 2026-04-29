package com.mealhunter.app.ui.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mealhunter.app.data.model.RecipeFilter
import com.mealhunter.app.ui.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onRecipeClick: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var showFilterSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "MealHunter",
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = { showFilterSheet = true }) {
                        Icon(Icons.Default.FilterList, contentDescription = "Filters")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                uiState.isLoading -> {
                    LoadingIndicator()
                }
                uiState.error != null -> {
                    ErrorMessage(
                        message = uiState.error!!,
                        onRetry = { viewModel.loadRecipes() }
                    )
                }
                else -> {
                    val currentRecipe = viewModel.getCurrentRecipe()
                    if (currentRecipe != null) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(modifier = Modifier.height(8.dp))

                            // Active filter chips
                            ActiveFilterChips(
                                filter = uiState.filter,
                                onClearFilter = { viewModel.updateFilter(RecipeFilter()) }
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Swipeable card
                            key(currentRecipe.id) {
                                SwipeableRecipeCard(
                                    recipe = currentRecipe,
                                    onSwipeLeft = { viewModel.onSwipeLeft() },
                                    onSwipeRight = { viewModel.onSwipeRight(currentRecipe) },
                                    onClick = { onRecipeClick(currentRecipe.id) },
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Action buttons
                            SwipeButtonsRow(
                                onSkip = { viewModel.onSwipeLeft() },
                                onSave = { viewModel.onSwipeRight(currentRecipe) }
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Counter
                            Text(
                                text = "${uiState.currentIndex + 1} / ${uiState.recipes.size}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    } else {
                        EmptyState(
                            message = "No recipes found. Try different filters!",
                            onAction = { viewModel.loadRecipes() },
                            actionText = "Refresh"
                        )
                    }
                }
            }
        }
    }

    // Filter bottom sheet
    if (showFilterSheet) {
        FilterBottomSheet(
            currentFilter = uiState.filter,
            onApply = { filter ->
                viewModel.updateFilter(filter)
                showFilterSheet = false
            },
            onDismiss = { showFilterSheet = false }
        )
    }
}

@Composable
private fun ActiveFilterChips(
    filter: RecipeFilter,
    onClearFilter: () -> Unit
) {
    if (filter != RecipeFilter()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            filter.cuisine?.let {
                AssistChip(
                    onClick = { },
                    label = { Text(it) }
                )
            }
            filter.diet?.let {
                AssistChip(
                    onClick = { },
                    label = { Text(it) }
                )
            }
            filter.type?.let {
                AssistChip(
                    onClick = { },
                    label = { Text(it) }
                )
            }
            TextButton(onClick = onClearFilter) {
                Text("Clear")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilterBottomSheet(
    currentFilter: RecipeFilter,
    onApply: (RecipeFilter) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedCuisine by remember { mutableStateOf(currentFilter.cuisine) }
    var selectedDiet by remember { mutableStateOf(currentFilter.diet) }
    var selectedType by remember { mutableStateOf(currentFilter.type) }
    var maxTime by remember { mutableStateOf(currentFilter.maxReadyTime?.toString() ?: "") }

    val cuisines = listOf("Italian", "Mexican", "Chinese", "Japanese", "Indian", "Thai", "French", "Mediterranean", "American", "Korean")
    val diets = listOf("Vegetarian", "Vegan", "Gluten Free", "Ketogenic", "Paleo")
    val mealTypes = listOf("Main Course", "Dessert", "Appetizer", "Salad", "Breakfast", "Soup", "Snack", "Drink")

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Text(
                "Filters",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(20.dp))

            // Cuisine
            Text("Cuisine", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            ChipGroup(
                items = cuisines,
                selected = selectedCuisine,
                onSelect = { selectedCuisine = if (selectedCuisine == it) null else it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Diet
            Text("Diet", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            ChipGroup(
                items = diets,
                selected = selectedDiet,
                onSelect = { selectedDiet = if (selectedDiet == it) null else it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Meal type
            Text("Meal Type", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            ChipGroup(
                items = mealTypes,
                selected = selectedType,
                onSelect = { selectedType = if (selectedType == it) null else it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Max time
            OutlinedTextField(
                value = maxTime,
                onValueChange = { maxTime = it.filter { c -> c.isDigit() } },
                label = { Text("Max cooking time (min)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { onApply(RecipeFilter()) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Reset")
                }
                Button(
                    onClick = {
                        onApply(
                            RecipeFilter(
                                cuisine = selectedCuisine,
                                diet = selectedDiet,
                                type = selectedType,
                                maxReadyTime = maxTime.toIntOrNull()
                            )
                        )
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Apply")
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ChipGroup(
    items: List<String>,
    selected: String?,
    onSelect: (String) -> Unit
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items.forEach { item ->
            FilterChip(
                selected = selected == item,
                onClick = { onSelect(item) },
                label = { Text(item) }
            )
        }
    }
}

@Composable
private fun EmptyState(
    message: String,
    onAction: () -> Unit,
    actionText: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "🍽️",
            style = MaterialTheme.typography.displayLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onAction) {
            Text(actionText)
        }
    }
}
