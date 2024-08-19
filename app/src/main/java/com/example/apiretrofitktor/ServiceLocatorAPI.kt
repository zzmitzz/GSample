package com.example.apiretrofitktor

import com.example.apiretrofitktor.data.remote.retrofit.ApiServicePokemon
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Before Dependency Injection enter the playground
object ServiceLocatorAPI {
    private var API_LINK: String = "https://pokeapi.co/api/v2/"
    var retrofit : Retrofit? = null
    val httpLoggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)
    private val okHttpClient: OkHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor { chain ->
                val request =
                    chain
                        .request()
                        .newBuilder()
                        .addHeader("Content-Type", "application/json")
                        .build()
                chain.proceed(request)
            }
            .addInterceptor(httpLoggingInterceptor)
            .build()

    fun getApiService() : ApiServicePokemon {
        if(retrofit == null){
            retrofit = Retrofit
                .Builder()
                .baseUrl(API_LINK)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!.create(ApiServicePokemon::class.java)
    }
}
