package com.mealhunter.app.data.api

import com.mealhunter.app.data.model.RecipeDto
import com.mealhunter.app.data.model.RecipeSearchResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SpoonacularApi {

    @GET("recipes/complexSearch")
    suspend fun searchRecipes(
        @Query("apiKey") apiKey: String,
        @Query("query") query: String? = null,
        @Query("cuisine") cuisine: String? = null,
        @Query("diet") diet: String? = null,
        @Query("type") type: String? = null,
        @Query("maxReadyTime") maxReadyTime: Int? = null,
        @Query("includeIngredients") includeIngredients: String? = null,
        @Query("number") number: Int = 20,
        @Query("offset") offset: Int = 0,
        @Query("addRecipeInformation") addRecipeInformation: Boolean = true,
        @Query("fillIngredients") fillIngredients: Boolean = true,
        @Query("sort") sort: String = "random"
    ): RecipeSearchResponse

    @GET("recipes/{id}/information")
    suspend fun getRecipeDetails(
        @Path("id") recipeId: Int,
        @Query("apiKey") apiKey: String,
        @Query("includeNutrition") includeNutrition: Boolean = false
    ): RecipeDto

    @GET("recipes/random")
    suspend fun getRandomRecipes(
        @Query("apiKey") apiKey: String,
        @Query("number") number: Int = 20,
        @Query("tags") tags: String? = null
    ): RandomRecipeResponse

    companion object {
        const val BASE_URL = "https://api.spoonacular.com/"
    }
}

data class RandomRecipeResponse(
    val recipes: List<RecipeDto>
)
