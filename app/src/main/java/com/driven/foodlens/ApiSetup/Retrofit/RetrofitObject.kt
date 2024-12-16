package com.driven.foodrecipeapp.ApiSetup.Retrofit

import com.driven.foodrecipeapp.ApiSetup.Service.ApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitObject {
    private const val API_URL = "https://api.spoonacular.com/recipes/"

    val client = OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.SECONDS) // Connect timeout
        .readTimeout(5, TimeUnit.SECONDS)    // Read timeout
        .writeTimeout(5, TimeUnit.SECONDS)   // Write timeout
        .build()

    private val retrofit by lazy {
        Retrofit.Builder().baseUrl(API_URL).addConverterFactory(GsonConverterFactory.create()).client(client).build()
    }

    val api by lazy{
        retrofit.create(ApiService::class.java)
    }
}