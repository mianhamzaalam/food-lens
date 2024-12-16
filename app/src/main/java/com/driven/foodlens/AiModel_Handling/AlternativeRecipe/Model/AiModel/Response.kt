package com.driven.foodrecipeapp.AiModel_Handling.AlternativeRecipe.Model.AiModel

data class Response(
    val alternative_directions: List<String>,
    val alternative_ingredients: List<AlternativeIngredient>,
    val recipe_name: String
)