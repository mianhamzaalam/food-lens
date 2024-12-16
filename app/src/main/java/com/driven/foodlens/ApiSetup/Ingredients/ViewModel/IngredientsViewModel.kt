package com.driven.foodrecipeapp.ApiSetup.Ingredients.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.driven.foodrecipeapp.ApiSetup.Ingredients.Model.ExtendedIngredient
import com.driven.foodrecipeapp.ApiSetup.Ingredients.Model.IngredientsModel
import com.driven.foodrecipeapp.ApiSetup.Ingredients.Repository.RecipeIngredientRepository
import kotlinx.coroutines.launch

class IngredientsViewModel(private val repository:RecipeIngredientRepository):ViewModel(){
    private val apiKey = "77a8163224fb477e8343482e44d14ebe"

    private val _data = MutableLiveData<IngredientsModel>()
    val data:LiveData<IngredientsModel> = _data

    fun getData(detailId: Int) {
        viewModelScope.launch {
            try {
                val response = repository.fetchData(detailId, apiKey)
                _data.postValue(response)
            } catch (e: Exception) {
                Log.d("ViewModel Ingredient","Error fetching ingredients: ${e.localizedMessage}")
            }
        }
    }
}