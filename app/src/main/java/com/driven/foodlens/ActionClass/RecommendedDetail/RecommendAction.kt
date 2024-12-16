package com.driven.foodrecipeapp.ActionClass.RecommendedDetail

import android.content.Context
import android.content.Intent
import com.driven.foodlens.Screens.MainDetailActivity
import com.driven.foodrecipeapp.ApiSetup.Model.Result
import com.driven.foodrecipeapp.ItemClickListener.RecommendListener


class RecommendAction(private val context:Context): RecommendListener {
    override fun onRecommend(result: Result) {
        var intent = Intent(context, MainDetailActivity::class.java).apply {
            putExtra("DR_ID",result.id)
            putExtra("DR_Title",result.title)
            putExtra("DR_Image",result.image)
        }
        context.startActivity(intent)
    }

}