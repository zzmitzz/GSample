package com.example.apiretrofitktor.data.model

import android.util.Log
import com.example.apiretrofitktor.ui.model.PokemonItem
import kotlinx.serialization.Serializable


@Serializable
data class Pokemon(
    val name: String,
    val url: String
)

fun Pokemon.toPokemonItem() : PokemonItem {
    val id = url.split("/".toRegex()).dropLast(1).last().toInt()
    Log.d("TAG", id.toString())
    return PokemonItem(
        image = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png",
        name = name,
        id = id
    )
}