package com.driven.foodrecipeapp.User.UserLogin.Object

import com.driven.foodrecipeapp.User.UserLogin.Service.UserLogService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object UserLogObject {

    val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(7, TimeUnit.SECONDS)  // Set connection timeout
        .writeTimeout(7, TimeUnit.SECONDS)    // Set write timeout
        .readTimeout(7, TimeUnit.SECONDS)     // Set read timeout
        .build()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://ahmarfypfoodlens-recipe-fyp.hf.space/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api by lazy {
        retrofit.create(UserLogService::class.java)
    }

}