package com.example.apiretrofitktor.data.remote

import com.example.apiretrofitktor.data.model.Pokemon
import com.example.apiretrofitktor.data.model.Response

interface NetworkService {
    suspend fun getPokemonList(limit: Int, offset: Int): Response
}