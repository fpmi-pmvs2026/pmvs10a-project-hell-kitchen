package com.mealhunter.app.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        FavoriteRecipeEntity::class,
        SearchHistoryEntity::class,
        MyIngredientEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class MealHunterDatabase : RoomDatabase() {

    abstract fun favoriteRecipeDao(): FavoriteRecipeDao
    abstract fun searchHistoryDao(): SearchHistoryDao
    abstract fun myIngredientDao(): MyIngredientDao

    companion object {
        @Volatile
        private var INSTANCE: MealHunterDatabase? = null

        fun getInstance(context: Context): MealHunterDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MealHunterDatabase::class.java,
                    "mealhunter_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
