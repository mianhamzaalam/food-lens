package com.driven.foodrecipeapp.ApiSetup.Model.SearchModel

data class SearchModel(
    val number: Int,
    val offset: Int,
    val results: List<SearchResult>,
    val totalResults: Int
)