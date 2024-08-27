package com.example.apiretrofitktor

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.apiretrofitktor.data.PokemonRepository
import com.example.apiretrofitktor.data.PokemonRepositoryImpl
import com.example.apiretrofitktor.ui.PokemonViewModel
import javax.inject.Inject

class PokemonViewModelFactory @Inject constructor (
    val pokemonRepositoryImpl: PokemonRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PokemonViewModel::class.java)) {

            @Suppress("UNCHECKED_CAST")
            return PokemonViewModel(pokemonRepositoryImpl) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}