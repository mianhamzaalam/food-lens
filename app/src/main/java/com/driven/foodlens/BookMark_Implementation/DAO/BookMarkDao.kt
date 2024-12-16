package com.driven.foodrecipeapp.BookMark_Implementation.DAO

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.driven.foodrecipeapp.BookMark_Implementation.Entity.BookMark_Recipe

@Dao
interface BookMarkDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(bookMark: BookMark_Recipe)

    @Delete
    suspend fun delete(bookMark: BookMark_Recipe)

    @Query("SELECT * FROM bookmark_Recipes")
    fun getBookMark():LiveData<List<BookMark_Recipe>>

    @Query("DELETE FROM bookmark_Recipes")
    suspend fun clearAll()
}