package com.example.apiretrofitktor

import android.app.Application

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        ServiceLocatorAPI.apply {
            getApiService()
        }
    }
}