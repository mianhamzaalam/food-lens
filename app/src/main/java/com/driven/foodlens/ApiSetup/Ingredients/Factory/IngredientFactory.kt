package com.driven.foodrecipeapp.ApiSetup.Ingredients.Factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.driven.foodrecipeapp.ApiSetup.Ingredients.Repository.RecipeIngredientRepository
import com.driven.foodrecipeapp.ApiSetup.Ingredients.ViewModel.IngredientsViewModel

class IngredientFactory(private val repository: RecipeIngredientRepository):ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(IngredientsViewModel::class.java)){
            return IngredientsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}