package com.driven.foodrecipeapp.ActionClass.SearchDetail

import android.content.Context
import android.content.Intent
import com.driven.foodlens.Screens.MainDetailActivity
import com.driven.foodrecipeapp.ApiSetup.Model.SearchModel.SearchResult
import com.driven.foodrecipeapp.ItemClickListener.SearchItemListener


class SearchItem(private val context: Context):SearchItemListener{
    override fun onSearchItemClick(recipe: SearchResult) {
        var intent = Intent(context, MainDetailActivity::class.java).apply {
            putExtra("DS_ID",recipe.id)
            putExtra("DS_Title",recipe.title)
            putExtra("DS_Image",recipe.image)
        }
        context.startActivity(intent)
    }
}