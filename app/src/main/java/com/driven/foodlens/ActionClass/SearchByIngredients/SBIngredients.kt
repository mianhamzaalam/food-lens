package com.driven.foodrecipeapp.ActionClass.SearchByIngredients

import android.content.Context
import android.content.Intent
import com.driven.foodlens.Screens.MainDetailActivity
import com.driven.foodrecipeapp.ApiSetup.Model.SearchRecipeByIngredients.SearchRecipeIngredientModelItem
import com.driven.foodrecipeapp.ItemClickListener.SearchByIngredientListener

class SBIngredients(private val context: Context): SearchByIngredientListener {
    override fun onSearchIngredientViewClick(recipe: SearchRecipeIngredientModelItem) {
        var intent = Intent(context, MainDetailActivity::class.java).apply {
            putExtra("DSB_ID",recipe.id)
            putExtra("DSB_Title",recipe.title)
            putExtra("DSB_Image",recipe.image)
        }
        context.startActivity(intent)
    }
}