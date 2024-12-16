package com.driven.foodrecipeapp.AiModel_Handling.Recipe.Service

import com.driven.foodrecipeapp.AiModel_Handling.Recipe.Model.AiRecipe.AiRecipeModel
import com.driven.foodrecipeapp.AiModel_Handling.Recipe.Model.RecipeData.RecipeData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface RecipeService {
    @POST("get_recipe/{token}")
    suspend fun getRecipeModel(@Path("token") token:String,@Body recipeData: RecipeData):AiRecipeModel
}