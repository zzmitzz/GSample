package com.example.apiretrofitktor.data.local

import com.example.apiretrofitktor.data.local.entity.PokemonEntity
import javax.inject.Inject


interface LocalService{
    suspend fun getAllPokemon(): List<PokemonEntity>

    suspend fun getPokemonById(id: Int): PokemonEntity

    suspend fun insertAllPokemon(list: List<PokemonEntity>): List<Long>

    suspend fun deletePokemon(pokemonEntity: PokemonEntity)

    suspend fun dumpTable()
}

class LocalDataSource @Inject constructor(
    private val localService: LocalService
) {
    suspend fun getAllPokemon() = localService.getAllPokemon()

    suspend fun getPokemonById(id: Int) = localService.getPokemonById(id)

    suspend fun insertAllPokemon(list: List<PokemonEntity>) = localService.insertAllPokemon(list)

    suspend fun dumpTable() = localService.dumpTable()
}