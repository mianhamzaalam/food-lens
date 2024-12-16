package com.driven.foodrecipeapp.BookMark_Implementation.Entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmark_Recipes")
data class BookMark_Recipe(
    @PrimaryKey(autoGenerate = true)
    var id:Int,

    @ColumnInfo("title")
    var title:String,

    @ColumnInfo("image")
    var image:String,

    @ColumnInfo("state")
    var state:Boolean
)
