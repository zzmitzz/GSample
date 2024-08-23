package com.example.apiretrofitktor.data

import android.content.Context
import com.example.apiretrofitktor.data.local.room.PokemonLocalDAO
import com.example.apiretrofitktor.data.local.room.RoomBuilder
import com.example.apiretrofitktor.data.remote.ktor.KtorClient
import com.example.apiretrofitktor.data.remote.retrofit.ApiServicePokemon
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Before Dependency Injection enter the playground
object ServiceLocatorAPI {
    var API_LINK: String = "https://pokeapi.co/api/v2/"
    var retrofit : Retrofit? = null
    var ktorClient: KtorClient? = null
    var localDB: PokemonLocalDAO? = null
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

    fun initKtorClient(): KtorClient{
        if(ktorClient == null){
            ktorClient = KtorClient()
        }
        return ktorClient!!
    }

    fun initRoomDB(applicationContext: Context): PokemonLocalDAO?{
        localDB = RoomBuilder.getInstance(applicationContext).pokemonLocalDAO()
        return localDB
    }
}
