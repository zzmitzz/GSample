package com.example.apiretrofitktor.ui.mvi

import android.net.Network
import com.example.apiretrofitktor.base.ViewIntent

sealed class PokemonIntent : ViewIntent {
    data class LoadingPokemon(val network: Boolean, val offset: Int, val limit: Int) : PokemonIntent()
    data object DeleteDb : PokemonIntent()
}