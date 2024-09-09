package com.example.apiretrofitktor.data

import android.util.Log
import com.example.apiretrofitktor.data.local.entity.PokemonEntity
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
                val listPokemonEntity = list.map { it ->
                    it.toPokemonEntity()
                }
                insertPokemon(listPokemonEntity)

                return ResultOf.Success(list)
            }catch(e: Exception){
                return ResultOf.Error("Network Error", e)
            }
        } else {
            val list: MutableList<Pokemon> = mutableListOf()
            for(i in offset..<offset+limit){
                try {
                    val pokemonEntity = pokemonLocalDAO.getPokemonById(i)
                    list.add(pokemonEntity.toPokemon())
                }catch (e: Exception){
                    return ResultOf.Error("Not have data",e)
                }
            }
            return ResultOf.Success(list)
        }
    }

    override suspend fun insertPokemon(list: List<PokemonEntity>) {
        pokemonLocalDAO.insertAllPokemon(list)
    }

    override suspend fun deleteOfflineCache() {
        pokemonLocalDAO.dumpTable()
    }
}


