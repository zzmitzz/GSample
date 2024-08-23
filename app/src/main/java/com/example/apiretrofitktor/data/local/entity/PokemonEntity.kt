package com.example.apiretrofitktor.data.local.entity

import android.graphics.BitmapFactory
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.apiretrofitktor.ui.model.PokemonItem


@Entity(tableName = "pokemon_character")
data class PokemonEntity (
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0,
    val name: String,
    val image: ByteArray
)

fun PokemonEntity.toPokemonItem(): PokemonItem {
    return PokemonItem(
        image = BitmapFactory.decodeByteArray(image, 0, image.size),
        name = name,
        id = id
    )
}