package com.driven.foodrecipeapp.User.UserLogin.Service

import com.driven.foodrecipeapp.User.UserLogin.Model.UserData
import com.driven.foodrecipeapp.User.UserLogin.Model.UserToken
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserLogService {

    @POST("get_token")
    suspend fun getToken(@Body userData: UserData): Response<UserToken>
}