package com.driven.foodrecipeapp.BookMark_Implementation.BookMarkFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.driven.foodrecipeapp.BookMark_Implementation.BookMarkRepository.BookMark_Repository
import com.driven.foodrecipeapp.BookMark_Implementation.BookMarkViewModel.BookMark_ViewModel

class BookFactory(private val repository: BookMark_Repository) :ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(BookMark_ViewModel::class.java)){
            return BookMark_ViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}