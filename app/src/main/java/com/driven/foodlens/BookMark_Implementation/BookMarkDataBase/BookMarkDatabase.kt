package com.driven.foodrecipeapp.BookMark_Implementation.BookMarkDataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.driven.foodrecipeapp.BookMark_Implementation.DAO.BookMarkDao
import com.driven.foodrecipeapp.BookMark_Implementation.Entity.BookMark_Recipe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Database(entities = [BookMark_Recipe::class], version = 1, exportSchema = false)
abstract class BookMarkDatabase :RoomDatabase(){

    abstract fun dao():BookMarkDao

    companion object{
        private const val DATABASE_NAME = "bookmark_db"
        @Volatile
        private var Instance:BookMarkDatabase?=null

        fun getDataBase(context: Context):BookMarkDatabase{

            synchronized(this){
                var instance = Instance
                if(instance==null){
                    instance = Room.databaseBuilder(context.applicationContext,
                        BookMarkDatabase::class.java, DATABASE_NAME)
                        .fallbackToDestructiveMigrationFrom()
                        .build()
                }

                return instance
            }
        }
    }

    suspend fun deleteDatabase(context: Context) {
        CoroutineScope(Dispatchers.IO).launch { context.deleteDatabase("bookmark_db") }
    }
}