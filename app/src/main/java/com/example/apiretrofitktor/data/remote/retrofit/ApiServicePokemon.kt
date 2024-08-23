package com.example.apiretrofitktor.data.remote.retrofit

import com.example.apiretrofitktor.data.remote.model.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServicePokemon {
    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
    ): Response
}
