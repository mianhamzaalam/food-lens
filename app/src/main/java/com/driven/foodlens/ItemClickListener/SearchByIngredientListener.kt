package com.driven.foodrecipeapp.ItemClickListener

import com.driven.foodrecipeapp.ApiSetup.Model.SearchRecipeByIngredients.SearchRecipeIngredientModelItem

interface SearchByIngredientListener {

    fun onSearchIngredientViewClick(recipe: SearchRecipeIngredientModelItem)
}