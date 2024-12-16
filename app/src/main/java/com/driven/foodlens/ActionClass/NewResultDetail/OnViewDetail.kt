package com.driven.foodrecipeapp.ActionClass.NewResultDetail

import android.content.Context
import android.content.Intent
import com.driven.foodlens.Screens.MainDetailActivity
import com.driven.foodrecipeapp.ApiSetup.Model.NewRecipe.NewResult
import com.driven.foodrecipeapp.ItemClickListener.ViewDetailClickListener

class OnViewDetail(private val context: Context): ViewDetailClickListener {
    override fun onViewClick(result: NewResult) {
        var intent = Intent(context, MainDetailActivity::class.java).apply {
            putExtra("DN_ID",result.id)
            putExtra("DN_Title",result.title)
            putExtra("DN_Image",result.image)
        }
        context.startActivity(intent)
    }
}