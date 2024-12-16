package com.driven.foodrecipeapp.ApiSetup.Model.Seasonal

data class SeasonModel(
    val number: Int,
    val offset: Int,
    val results: List<SeasonResult>,
    val totalResults: Int
)