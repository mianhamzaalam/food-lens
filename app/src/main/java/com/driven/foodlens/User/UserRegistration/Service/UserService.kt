package com.driven.foodrecipeapp.User.UserRegistration.Service

import com.driven.foodrecipeapp.User.UserRegistration.Model.User
import com.driven.foodrecipeapp.User.UserRegistration.Model.UserRegistration
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {
    @POST("register")
    suspend fun registerUser(@Body user: User) : Response<UserRegistration>
}