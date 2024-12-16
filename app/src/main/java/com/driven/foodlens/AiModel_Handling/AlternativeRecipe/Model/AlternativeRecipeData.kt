package com.driven.foodrecipeapp.AiModel_Handling.AlternativeRecipe.Model

data class AlternativeRecipeData (
    val recipe_name: String,
    val dietary_restrictions: String,
    val allergies: List<String>
)