package com.driven.foodrecipeapp.Bookmark_Listener

import com.driven.foodrecipeapp.ApiSetup.Model.NewRecipe.NewResult
import com.driven.foodrecipeapp.ApiSetup.Model.Seasonal.SeasonResult

interface BookMarkService {
    fun addToBookMark(result:NewResult)
}