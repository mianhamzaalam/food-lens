package com.driven.foodrecipeapp.ApiSetup.Service

import com.driven.foodrecipeapp.ApiSetup.Model.NewRecipe.NewRecipeModel
import com.driven.foodrecipeapp.ApiSetup.Model.RecipeModel
import com.driven.foodrecipeapp.ApiSetup.Model.SearchModel.SearchModel
import com.driven.foodrecipeapp.ApiSetup.Model.SearchRecipeByIngredients.SearchRecipeIngredientModelItem
import com.driven.foodrecipeapp.ApiSetup.Model.Seasonal.SeasonModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("complexSearch")
    suspend fun getRecipes(@Query("apiKey") api:String,
                   @Query("type") type:String,
                   @Query("number") number:Int):RecipeModel

    @GET("findByIngredients")
    suspend fun getSearchIngredientsRecipes(
        @Query("ingredients") ingredients:String,
        @Query("number") results:Int,
        @Query("apiKey") key:String): List<SearchRecipeIngredientModelItem>

    @GET("complexSearch")
    suspend fun getNewRecipe(@Query("sort") sort:String,
                     @Query("apiKey") apiKey:String,
                     @Query("number") number:Int):NewRecipeModel

    @GET("complexSearch")
    suspend fun getSeasonRecipe(@Query("season") season:String,
                                @Query("apiKey") apiKey:String,
                                @Query("number") number:Int): SeasonModel

    @GET("complexSearch")
    suspend fun SearchRecipes(@Query("apiKey") apiKey:String,
                              @Query("query") query:String,
                              @Query("number") number:Int): SearchModel
}