package com.example.apiretrofitktor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.apiretrofitktor.data.remote.NetworkService
import com.example.apiretrofitktor.ui.PokemonViewModel

class PokemonViewModelFactory(
    private val networkService: NetworkService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PokemonViewModel::class.java)) {

            @Suppress("UNCHECKED_CAST")
            return PokemonViewModel(networkService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}