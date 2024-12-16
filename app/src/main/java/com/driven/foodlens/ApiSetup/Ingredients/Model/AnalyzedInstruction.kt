package com.driven.foodrecipeapp.ApiSetup.Ingredients.Model

data class AnalyzedInstruction(
    val name: String,
    val steps: List<Step>
)