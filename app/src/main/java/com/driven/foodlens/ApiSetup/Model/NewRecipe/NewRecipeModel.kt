package com.driven.foodrecipeapp.ApiSetup.Model.NewRecipe

data class NewRecipeModel(
    val number: Int,
    val offset: Int,
    val results: List<NewResult>,
    val totalResults: Int
)