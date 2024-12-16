package com.driven.foodrecipeapp.AiModel_Handling.Recipe.Model.AiRecipe

data class Response(
    val directions: List<String>,
    val ingredients: List<Ingredient>,
    val recipe_name: String
)