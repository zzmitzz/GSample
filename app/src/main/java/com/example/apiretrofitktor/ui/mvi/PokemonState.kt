package com.example.apiretrofitktor.ui.mvi

import com.example.apiretrofitktor.ui.model.Pokemon


data class PokemonState(
    val loading: Boolean = true,
    val error: String? = null,
    val listPokemon: List<Pokemon> = emptyList(),
)
