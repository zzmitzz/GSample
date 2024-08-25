package com.example.apiretrofitktor.data

import okhttp3.logging.HttpLoggingInterceptor

// Before Dependency Injection enter the playground
object Constant {
    var API_LINK: String = "https://pokeapi.co/api/v2/"
    val httpLoggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)
}
