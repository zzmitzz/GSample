package com.example.apiretrofitktor.data.remote.retrofit

import com.example.apiretrofitktor.data.remote.model.Response
import com.example.apiretrofitktor.data.remote.NetworkService
import retrofit2.Retrofit

class RetrofitService(private val retrofit: ApiServicePokemon) : NetworkService {
    override suspend fun getPokemonList(limit: Int, offset: Int): Response {
        return retrofit.getPokemonList(limit, offset)
    }
}