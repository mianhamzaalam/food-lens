package com.driven.foodrecipeapp.AiModel_Handling.ImagePrediction.Object

import com.driven.foodrecipeapp.AiModel_Handling.ImagePrediction.Service.ImagePredictService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ImagePredictObject {

    val client = OkHttpClient.Builder()
        .connectTimeout(20, TimeUnit.SECONDS) // Connect timeout
        .readTimeout(20, TimeUnit.SECONDS)    // Read timeout
        .writeTimeout(20, TimeUnit.SECONDS)   // Write timeout
        .build()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://ahmarfypfoodlens-recipe-fyp.hf.space/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    val api by lazy {
        retrofit.create(ImagePredictService::class.java)
    }
}