package com.driven.foodrecipeapp.ItemClickListener

import com.driven.foodrecipeapp.ApiSetup.Model.Result

interface RecommendListener {
    fun onRecommend(result: Result)
}