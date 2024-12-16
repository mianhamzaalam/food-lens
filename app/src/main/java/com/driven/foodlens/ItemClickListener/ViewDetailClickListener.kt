package com.driven.foodrecipeapp.ItemClickListener

import com.driven.foodrecipeapp.ApiSetup.Model.NewRecipe.NewResult

interface ViewDetailClickListener {
    fun onViewClick(result:NewResult)
}