package com.example.apiretrofitktor.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "pokemon_character")
data class PokemonEntity (
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val name: String,
    val image: ByteArray
)
