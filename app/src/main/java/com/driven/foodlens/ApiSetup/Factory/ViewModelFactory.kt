package com.driven.foodrecipeapp.ApiSetup.Factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.driven.foodrecipeapp.ApiSetup.Repository.RecipeRepository
import com.driven.foodrecipeapp.ApiSetup.ViewModel.RecipeViewModel

class ViewModelFactory(private val repository: RecipeRepository):ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipeViewModel::class.java)) {
            return RecipeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}