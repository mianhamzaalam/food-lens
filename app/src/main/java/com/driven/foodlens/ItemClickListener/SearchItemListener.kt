package com.driven.foodrecipeapp.ItemClickListener

import com.driven.foodrecipeapp.ApiSetup.Model.SearchModel.SearchResult

interface SearchItemListener {
    fun onSearchItemClick(recipe: SearchResult)
}