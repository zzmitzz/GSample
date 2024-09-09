package com.example.apiretrofitktor.data.local.entity

import android.graphics.BitmapFactory
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.apiretrofitktor.ui.model.Pokemon


@Entity(tableName = "pokemon_character")
data class PokemonEntity (
    @PrimaryKey(autoGenerate = false)
    var id: Int,
    val name: String,
    val image: ByteArray
)

fun PokemonEntity.toPokemon(): Pokemon {
    return Pokemon(
        image = BitmapFactory.decodeByteArray(this.image, 0, this.image.size),
        name = this.name,
        id = this.id
    )
}