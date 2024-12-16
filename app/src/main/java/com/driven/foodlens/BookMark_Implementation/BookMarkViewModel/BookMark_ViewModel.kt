package com.driven.foodrecipeapp.BookMark_Implementation.BookMarkViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.driven.foodrecipeapp.BookMark_Implementation.BookMarkRepository.BookMark_Repository
import com.driven.foodrecipeapp.BookMark_Implementation.Entity.BookMark_Recipe
import kotlinx.coroutines.launch

class BookMark_ViewModel(private val repository: BookMark_Repository) :ViewModel() {

    val savedRecipes = repository.bookMark

    fun insert(bookmarkRecipe: BookMark_Recipe)=viewModelScope.launch {
        repository.insert(bookmarkRecipe)
    }
    fun resetDataBase() = viewModelScope.launch {
        repository.resetDataBase()
    }
}