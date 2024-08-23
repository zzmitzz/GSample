package com.example.apiretrofitktor.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.apiretrofitktor.data.local.entity.PokemonEntity


@Database(entities = [PokemonEntity::class], version = 2)
abstract class AppDataBase: RoomDatabase() {
    abstract fun pokemonLocalDAO(): PokemonLocalDAO
}