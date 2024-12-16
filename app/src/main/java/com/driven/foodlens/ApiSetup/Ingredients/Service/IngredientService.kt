package com.driven.foodrecipeapp.ApiSetup.Ingredients.Service

import com.driven.foodrecipeapp.ApiSetup.Ingredients.Model.IngredientsModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface IngredientService {

    @GET("{id}/information")
    suspend fun getIngredients(
        @Path("id") id:Int,
        @Query("apiKey") apiKey:String
    ):IngredientsModel
}