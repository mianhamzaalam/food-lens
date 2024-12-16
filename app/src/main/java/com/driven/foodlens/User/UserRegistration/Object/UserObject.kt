package com.driven.foodrecipeapp.User.UserRegistration.Object

import com.driven.foodrecipeapp.User.UserRegistration.Service.UserService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object UserObject {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://ahmarfypfoodlens-recipe-fyp.hf.space/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api by lazy {
        retrofit.create(UserService::class.java)
    }
}