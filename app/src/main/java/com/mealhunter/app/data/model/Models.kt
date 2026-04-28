package com.mealhunter.app.data.model

import com.google.gson.annotations.SerializedName

// --- API Response Models ---

data class RecipeSearchResponse(
    @SerializedName("results") val results: List<RecipeDto>,
    @SerializedName("offset") val offset: Int,
    @SerializedName("number") val number: Int,
    @SerializedName("totalResults") val totalResults: Int
)

data class RecipeDto(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("image") val image: String?,
    @SerializedName("imageType") val imageType: String?,
    @SerializedName("readyInMinutes") val readyInMinutes: Int? = null,
    @SerializedName("servings") val servings: Int? = null,
    @SerializedName("sourceUrl") val sourceUrl: String? = null,
    @SerializedName("summary") val summary: String? = null,
    @SerializedName("cuisines") val cuisines: List<String>? = null,
    @SerializedName("dishTypes") val dishTypes: List<String>? = null,
    @SerializedName("diets") val diets: List<String>? = null,
    @SerializedName("instructions") val instructions: String? = null,
    @SerializedName("extendedIngredients") val extendedIngredients: List<IngredientDto>? = null,
    @SerializedName("analyzedInstructions") val analyzedInstructions: List<AnalyzedInstruction>? = null,
    @SerializedName("vegetarian") val vegetarian: Boolean? = null,
    @SerializedName("vegan") val vegan: Boolean? = null,
    @SerializedName("glutenFree") val glutenFree: Boolean? = null,
    @SerializedName("dairyFree") val dairyFree: Boolean? = null,
    @SerializedName("healthScore") val healthScore: Double? = null
)

data class IngredientDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("original") val original: String,
    @SerializedName("amount") val amount: Double,
    @SerializedName("unit") val unit: String,
    @SerializedName("image") val image: String?
)

data class AnalyzedInstruction(
    @SerializedName("name") val name: String,
    @SerializedName("steps") val steps: List<InstructionStep>
)

data class InstructionStep(
    @SerializedName("number") val number: Int,
    @SerializedName("step") val step: String
)

// --- Domain Model ---

data class Recipe(
    val id: Int,
    val title: String,
    val image: String?,
    val readyInMinutes: Int,
    val servings: Int,
    val summary: String,
    val cuisines: List<String>,
    val dishTypes: List<String>,
    val diets: List<String>,
    val ingredients: List<Ingredient>,
    val steps: List<String>,
    val vegetarian: Boolean,
    val vegan: Boolean,
    val glutenFree: Boolean,
    val dairyFree: Boolean,
    val healthScore: Double,
    val isFavorite: Boolean = false
)

data class Ingredient(
    val id: Int,
    val name: String,
    val original: String,
    val amount: Double,
    val unit: String,
    val image: String?
)

// --- Filter Model ---

data class RecipeFilter(
    val cuisine: String? = null,
    val diet: String? = null,
    val type: String? = null,
    val query: String? = null,
    val maxReadyTime: Int? = null,
    val includeIngredients: String? = null
)

// --- Mapper ---

fun RecipeDto.toDomain(isFavorite: Boolean = false): Recipe = Recipe(
    id = id,
    title = title,
    image = image,
    readyInMinutes = readyInMinutes ?: 0,
    servings = servings ?: 0,
    summary = summary?.replace(Regex("<[^>]*>"), "") ?: "",
    cuisines = cuisines ?: emptyList(),
    dishTypes = dishTypes ?: emptyList(),
    diets = diets ?: emptyList(),
    ingredients = extendedIngredients?.map { it.toDomain() } ?: emptyList(),
    steps = analyzedInstructions?.firstOrNull()?.steps?.map { it.step } ?: emptyList(),
    vegetarian = vegetarian ?: false,
    vegan = vegan ?: false,
    glutenFree = glutenFree ?: false,
    dairyFree = dairyFree ?: false,
    healthScore = healthScore ?: 0.0,
    isFavorite = isFavorite
)

fun IngredientDto.toDomain(): Ingredient = Ingredient(
    id = id,
    name = name,
    original = original,
    amount = amount,
    unit = unit,
    image = image?.let { "https://spoonacular.com/cdn/ingredients_100x100/$it" }
)
