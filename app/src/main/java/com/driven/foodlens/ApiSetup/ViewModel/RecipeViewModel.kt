package com.driven.foodrecipeapp.ApiSetup.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.driven.foodrecipeapp.ApiSetup.Model.NewRecipe.NewResult
import com.driven.foodrecipeapp.ApiSetup.Model.Result
import com.driven.foodrecipeapp.ApiSetup.Model.SearchModel.SearchResult
import com.driven.foodrecipeapp.ApiSetup.Model.SearchRecipeByIngredients.SearchRecipeIngredientModelItem
import com.driven.foodrecipeapp.ApiSetup.Model.Seasonal.SeasonResult
import com.driven.foodrecipeapp.ApiSetup.Repository.RecipeRepository
import kotlinx.coroutines.launch

class RecipeViewModel(private val repository: RecipeRepository) : ViewModel() {

    // LiveData for recipes
    private val _data = MutableLiveData<List<Result>>()
    val data: LiveData<List<Result>> get() = _data

    // LiveData for seasonal recipes
    private val _seasonalData = MutableLiveData<List<SeasonResult>>()
    val seasonalData: LiveData<List<SeasonResult>> get() = _seasonalData

    // LiveData for search results
    private val _searchData = MutableLiveData<List<SearchResult>>()
    val searchData: LiveData<List<SearchResult>> get() = _searchData

    // LiveData for new recipes
    private val _newRecipeData = MutableLiveData<List<NewResult>>()
    val newRecipeData: LiveData<List<NewResult>> get() = _newRecipeData

    // LiveData for recipes by ingredients
    private val _ingredientsSearchData = MutableLiveData<List<SearchRecipeIngredientModelItem>>()
    val ingredientsSearchData: LiveData<List<SearchRecipeIngredientModelItem>> get() = _ingredientsSearchData

    // Fetch recipes using coroutines
    fun fetchRecipes(apiKey: String, type: String, number: Int) {
        Log.d("RecipeViewModel", "Fetching recipes...")
        viewModelScope.launch {
            try {
                val response = repository.getRecipes(apiKey, type, number)
                Log.d("RecipeViewModel", "Response received: ${response.results.size} items")
                _data.postValue(response.results)
            } catch (e: Exception) {
                Log.e("RecipeViewModel", "Error fetching recipes: ${e.message}")
            }
        }
    }

    // Fetch seasonal recipes
    fun fetchSeasonalRecipes(season: String, apiKey: String, number: Int) {
        viewModelScope.launch {
            try {
                // Make the API call and handle response
                val response = repository.getSeasonRecipe(season, apiKey, number)
                _seasonalData.postValue(response.results)  // Assuming response.results contains seasonal recipe data
            } catch (e: Exception) {
                // Handle error
                Log.e("RecipeViewModel", "Error fetching seasonal recipes: ${e.message}")
            }
        }
    }

    // Fetch search recipes
    fun fetchSearchRecipes(apiKey: String, query: String, number: Int) {
        viewModelScope.launch {
            try {
                // Make the API call and handle response
                val response = repository.searchRecipes(apiKey, query, number)
                _searchData.postValue(response.results)  // Assuming response.results contains search result data
            } catch (e: Exception) {
                // Handle error
                Log.e("RecipeViewModel", "Error fetching search recipes: ${e.message}")
            }
        }
    }

    // Fetch new recipes
    fun fetchNewRecipes(sort: String, apiKey: String, number: Int) {
        viewModelScope.launch {
            try {
                // Make the API call and handle response
                val response = repository.getNewRecipes(sort, apiKey, number)
                _newRecipeData.postValue(response.results)  // Assuming response.results contains new recipe data
            } catch (e: Exception) {
                // Handle error
                Log.e("RecipeViewModel", "Error fetching new recipes: ${e.message}")
            }
        }
    }

    // Fetch recipes by ingredients
    fun fetchRecipesByIngredients(ingredients: String, results: Int, apiKey: String) {
        viewModelScope.launch {
            try {
                // Make the API call and handle response
                val response = repository.getSearchIngredientsRecipes(ingredients, results, apiKey)
                _ingredientsSearchData.postValue(response)  // Assuming the response is a list of recipes
            } catch (e: Exception) {
                // Handle error
                Log.e("RecipeViewModel", "Error fetching recipes by ingredients: ${e.message}")
            }
        }
    }
}
