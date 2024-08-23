
package com.example.apiretrofitktor.data.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.apiretrofitktor.data.local.LocalService
import com.example.apiretrofitktor.data.local.entity.PokemonEntity

@Dao
interface PokemonLocalDAO : LocalService {
    @Query("SELECT * FROM pokemon_character")
    override suspend fun getAllPokemon(): List<PokemonEntity>

    @Query("SELECT * FROM pokemon_character WHERE id = :id")
    override suspend fun getPokemonById(id: Int): PokemonEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insertAllPokemon(list: List<PokemonEntity>): List<Long>

    @Delete
    override suspend fun deletePokemon(pokemonEntity: PokemonEntity)

    @Query("Delete FROM pokemon_character")
    override suspend fun dumpTable()
}