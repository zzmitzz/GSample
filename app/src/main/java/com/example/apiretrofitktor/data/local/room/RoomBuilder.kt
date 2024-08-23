package com.example.apiretrofitktor.data.local.room

import android.content.Context
import androidx.room.Room

object RoomBuilder {
    @Volatile
    private var INSTANCE : AppDataBase? = null

    fun getInstance(context: Context): AppDataBase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDataBase::class.java,
                "app_database"
            ).fallbackToDestructiveMigration().build()
            INSTANCE = instance
            instance
        }
    }
}