package com.example.apiretrofitktor.data

import com.example.apiretrofitktor.data.local.entity.PokemonEntity
import com.example.apiretrofitktor.ui.model.Pokemon

interface PokemonRepository {
    suspend fun getPokemon(networkConnection: Boolean, limit: Int, offset: Int): ResultOf<List<Pokemon>>
    suspend fun insertPokemon(list: List<PokemonEntity>)
    suspend fun deleteOfflineCache()
}