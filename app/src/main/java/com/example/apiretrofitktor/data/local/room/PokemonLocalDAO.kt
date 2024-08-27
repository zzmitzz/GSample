
package com.example.apiretrofitktor.data.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.apiretrofitktor.data.local.entity.PokemonEntity

@Dao
interface PokemonLocalDAO {
    @Query("SELECT * FROM pokemon_character")
    suspend fun getAllPokemon(): List<PokemonEntity>

    @Query("SELECT * FROM pokemon_character WHERE id = :id")
    suspend fun getPokemonById(id: Int): PokemonEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPokemon(list: List<PokemonEntity>): List<Long>

    @Delete
    suspend fun deletePokemon(pokemonEntity: PokemonEntity)

    @Query("Delete FROM pokemon_character")
    suspend fun dumpTable()
}