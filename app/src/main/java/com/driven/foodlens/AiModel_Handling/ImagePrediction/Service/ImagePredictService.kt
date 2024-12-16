package com.driven.foodrecipeapp.AiModel_Handling.ImagePrediction.Service

import com.driven.foodrecipeapp.AiModel_Handling.ImagePrediction.Model.ApiResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ImagePredictService {
    @Multipart
    @POST("upload-image/{token}")
    suspend fun predictImage(
        @Path("token") token: String,
        @Part file: MultipartBody.Part): Response<ApiResponse>
}