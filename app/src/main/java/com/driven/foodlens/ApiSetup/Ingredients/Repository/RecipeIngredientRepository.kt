package com.driven.foodrecipeapp.ApiSetup.Ingredients.Repository

import com.driven.foodrecipeapp.ApiSetup.Ingredients.Model.IngredientsModel
import com.driven.foodrecipeapp.ApiSetup.Ingredients.Service.IngredientService

class RecipeIngredientRepository(val api: IngredientService) {

    suspend fun fetchData(detailId: Int,apiKey:String):IngredientsModel{
        return api.getIngredients(detailId,apiKey)
    }
}
