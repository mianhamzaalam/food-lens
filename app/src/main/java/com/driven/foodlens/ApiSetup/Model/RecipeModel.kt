package com.driven.foodrecipeapp.ApiSetup.Model

data class RecipeModel(
    val number: Int,
    val offset: Int,
    val results: List<Result>,
    val totalResults: Int
)