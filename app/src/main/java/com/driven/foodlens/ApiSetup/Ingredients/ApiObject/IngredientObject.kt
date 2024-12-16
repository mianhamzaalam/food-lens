package com.driven.foodrecipeapp.ApiSetup.Ingredients.ApiObject

import com.driven.foodrecipeapp.ApiSetup.Ingredients.Service.IngredientService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object IngredientObject {

    val client = OkHttpClient.Builder()
        .connectTimeout(7, TimeUnit.SECONDS) // Connect timeout
        .readTimeout(7, TimeUnit.SECONDS)    // Read timeout
        .writeTimeout(7, TimeUnit.SECONDS)   // Write timeout
        .build()

    private val retrofit by lazy {
        Retrofit.Builder().baseUrl("https://api.spoonacular.com/recipes/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    val api by lazy {
        retrofit.create(IngredientService::class.java)
    }
}