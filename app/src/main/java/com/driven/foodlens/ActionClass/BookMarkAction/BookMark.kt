package com.driven.foodrecipeapp.ActionClass.BookMarkAction

import android.content.Context
import com.driven.foodrecipeapp.ApiSetup.Model.NewRecipe.NewResult
import com.driven.foodrecipeapp.BookMark_Implementation.BookMarkDataBase.BookMarkDatabase
import com.driven.foodrecipeapp.BookMark_Implementation.Entity.BookMark_Recipe
import com.driven.foodrecipeapp.Bookmark_Listener.BookMarkService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookMark(private val context: Context):BookMarkService {

    private val bookmarkdao = BookMarkDatabase.getDataBase(context).dao()

    override fun addToBookMark(result:NewResult) {

        var title = result.title
        var image = result.image

        CoroutineScope(Dispatchers.IO).launch {
            bookmarkdao.insert(BookMark_Recipe(0,title,image,true))
        }

    }
}