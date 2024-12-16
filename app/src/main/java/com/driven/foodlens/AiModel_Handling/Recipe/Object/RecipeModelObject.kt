package com.driven.foodrecipeapp.AiModel_Handling.Recipe.Object

import com.driven.foodrecipeapp.AiModel_Handling.Recipe.Service.RecipeService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RecipeModelObject {

    private val client = OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.SECONDS)
        .readTimeout(5, TimeUnit.SECONDS)
        .writeTimeout(5, TimeUnit.SECONDS)
        .build()

    val api by lazy {
        Retrofit.Builder().baseUrl("https://ahmarfypfoodlens-recipe-fyp.hf.space/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(RecipeService::class.java)
    }
}