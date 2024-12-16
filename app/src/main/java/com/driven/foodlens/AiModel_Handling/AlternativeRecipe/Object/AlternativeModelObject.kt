package com.driven.foodrecipeapp.AiModel_Handling.AlternativeRecipe.Object

import com.driven.foodrecipeapp.AiModel_Handling.AlternativeRecipe.Service.AlternativeService
import com.driven.foodrecipeapp.AiModel_Handling.Recipe.Service.RecipeService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AlternativeModelObject {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://ahmarfypfoodlens-recipe-fyp.hf.space/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val api by lazy {
        retrofit.create(AlternativeService::class.java)
    }
}