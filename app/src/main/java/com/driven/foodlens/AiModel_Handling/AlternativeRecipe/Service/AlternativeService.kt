package com.driven.foodrecipeapp.AiModel_Handling.AlternativeRecipe.Service

import com.driven.foodrecipeapp.AiModel_Handling.AlternativeRecipe.Model.AiModel.AlternativeRecipe
import com.driven.foodrecipeapp.AiModel_Handling.AlternativeRecipe.Model.AlternativeRecipeData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface AlternativeService {
    @POST("get_recipe_alternative/{token}")
    fun getAlternatives(@Path("token") token:String,@Body alternativeRecipe: AlternativeRecipeData) : Call<AlternativeRecipe>
}