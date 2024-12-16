package com.driven.foodrecipeapp.ApiSetup.Model.NewRecipe

data class NewResult(
    val id: Int,
    val image: String,
    val imageType: String,
    val title: String,
    var status:Boolean
)