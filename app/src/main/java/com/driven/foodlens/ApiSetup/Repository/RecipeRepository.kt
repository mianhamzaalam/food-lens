package com.driven.foodrecipeapp.ApiSetup.Repository


import com.driven.foodrecipeapp.ApiSetup.Model.NewRecipe.NewRecipeModel
import com.driven.foodrecipeapp.ApiSetup.Model.RecipeModel
import com.driven.foodrecipeapp.ApiSetup.Model.SearchModel.SearchModel
import com.driven.foodrecipeapp.ApiSetup.Model.SearchRecipeByIngredients.SearchRecipeIngredientModelItem
import com.driven.foodrecipeapp.ApiSetup.Model.Seasonal.SeasonModel
import com.driven.foodrecipeapp.ApiSetup.Service.ApiService


class RecipeRepository(private val apiService: ApiService) {

    // Fetch recipes
    suspend fun getRecipes(apiKey: String, type: String, number: Int): RecipeModel {
        return apiService.getRecipes(apiKey, type, number)
    }

    // Fetch new recipes
    suspend fun getNewRecipes(sort: String, apiKey: String, number: Int): NewRecipeModel {
        return apiService.getNewRecipe(sort, apiKey, number)
    }

    // Fetch recipes by ingredients
    suspend fun getSearchIngredientsRecipes(ingredients: String, results: Int, key: String): List<SearchRecipeIngredientModelItem> {
        return apiService.getSearchIngredientsRecipes(ingredients, results, key)
    }

    // Fetch seasonal recipes
    suspend fun getSeasonRecipe(season: String, apiKey: String, number: Int): SeasonModel {
        return apiService.getSeasonRecipe(season, apiKey, number)
    }

    // Search recipes
    suspend fun searchRecipes(apiKey: String, query: String, number: Int): SearchModel {
        return apiService.SearchRecipes(apiKey, query, number)
    }

}