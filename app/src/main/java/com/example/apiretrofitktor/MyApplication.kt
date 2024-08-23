package com.example.apiretrofitktor

import android.app.Application
import com.example.apiretrofitktor.data.ServiceLocatorAPI
import com.example.apiretrofitktor.data.local.room.RoomBuilder

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        ServiceLocatorAPI.apply {
            getApiService()
            initRoomDB(applicationContext)
        }
        RoomBuilder.getInstance(this).pokemonLocalDAO()
    }
}