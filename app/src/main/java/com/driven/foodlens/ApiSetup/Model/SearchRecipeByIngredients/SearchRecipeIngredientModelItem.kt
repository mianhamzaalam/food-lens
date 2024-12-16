package com.driven.foodrecipeapp.ApiSetup.Model.SearchRecipeByIngredients

data class SearchRecipeIngredientModelItem(
    val id: Int,
    val image: String,
    val imageType: String,
    val likes: Int,
    val missedIngredientCount: Int,
    val missedIngredients: List<MissedIngredient>,
    val title: String,
    val unusedIngredients: List<Any>,
    val usedIngredientCount: Int,
    val usedIngredients: List<UsedIngredient>
)