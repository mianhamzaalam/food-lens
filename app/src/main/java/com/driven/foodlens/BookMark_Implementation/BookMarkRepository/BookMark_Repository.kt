package com.driven.foodrecipeapp.BookMark_Implementation.BookMarkRepository

import com.driven.foodrecipeapp.BookMark_Implementation.DAO.BookMarkDao
import com.driven.foodrecipeapp.BookMark_Implementation.Entity.BookMark_Recipe

class BookMark_Repository(private val bookMarkDao: BookMarkDao) {

    val bookMark = bookMarkDao.getBookMark()

    suspend fun insert(bookmarkRecipe: BookMark_Recipe) = bookMarkDao.insert(bookmarkRecipe)

    suspend fun resetDataBase() = bookMarkDao.clearAll()
}