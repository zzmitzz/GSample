package com.example.apiretrofitktor.data

import com.example.apiretrofitktor.data.local.LocalDataSource
import com.example.apiretrofitktor.data.local.entity.PokemonEntity
import com.example.apiretrofitktor.data.remote.NetworkDataSource
import com.example.apiretrofitktor.data.remote.NetworkService
import com.example.apiretrofitktor.data.remote.model.Response
import javax.inject.Inject

class PokemonRepository @Inject constructor(
    private val remoteDataSource: NetworkDataSource,
    private val localDataSource: LocalDataSource
) {
    suspend fun getPokemonRemote( limit: Int, offset: Int) = remoteDataSource.getPokemonList(limit, offset)
    suspend fun insertAllPokemon(list: List<PokemonEntity>) = localDataSource.insertAllPokemon(list)
    suspend fun getPokemonLocal() = localDataSource.getAllPokemon()
    suspend fun deleteRoomDb() = localDataSource.dumpTable()
}


