package com.example.apiretrofitktor.data

import com.example.apiretrofitktor.data.local.entity.toPokemon
import com.example.apiretrofitktor.data.local.room.PokemonLocalDAO
import com.example.apiretrofitktor.data.remote.model.toPokemon
import com.example.apiretrofitktor.data.remote.retrofit.ApiServicePokemon
import com.example.apiretrofitktor.ui.model.Pokemon
import com.example.apiretrofitktor.ui.model.toPokemonEntity
import javax.inject.Inject


class PokemonRepositoryImpl @Inject constructor(
    val apiServicePokemon: ApiServicePokemon,
    val pokemonLocalDAO: PokemonLocalDAO
) : PokemonRepository {
    override suspend fun getPokemon(
        networkConnection: Boolean,
        limit: Int,
        offset: Int
    ): ResultOf<List<Pokemon>> {
        if (networkConnection) {
            try {
                val list = apiServicePokemon.getPokemonList(limit, offset).results.map { it ->
                    it.toPokemon()
                }
                insertPokemon(list)
                return ResultOf.Success(list)
            }catch(e: Exception){
                return ResultOf.Error("Network Error", e)
            }
        } else {
            return ResultOf.Success(pokemonLocalDAO.getAllPokemon().map { it ->
                it.toPokemon()
            })
        }
    }

    override suspend fun insertPokemon(list: List<Pokemon>) {
        pokemonLocalDAO.insertAllPokemon(list.map { it ->
            it.toPokemonEntity()
        })
    }

    override suspend fun deleteOfflineCache() {
        pokemonLocalDAO.dumpTable()
    }
}


