package com.example.apiretrofitktor.ui

import android.net.Network
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apiretrofitktor.data.PokemonRepository
import com.example.apiretrofitktor.data.ResultOf
import com.example.apiretrofitktor.ui.model.Pokemon
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PokemonViewModel @Inject constructor(
    val pokemonRepositoryImpl: PokemonRepository
) : ViewModel() {

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    var isLoadMore: MutableStateFlow<Boolean> = MutableStateFlow(false)

    val listPokemon: MutableStateFlow<ResultOf<List<Pokemon>>> =
        MutableStateFlow(ResultOf.Loading)

    fun getPokemon(
        network: Boolean,
        limit: Int,
        offset: Int,
    ) {
        /*
        * This function fetch data from server and immediately insert to local database
         */
        viewModelScope.launch {
            _isLoading.value = true
            listPokemon.value = pokemonRepositoryImpl.getPokemon(network,limit,offset)
            isLoadMore.value = false
            _isLoading.value = false
        }
    }
    fun NukeDB(){
        viewModelScope.launch {
            _isLoading.value = true
            pokemonRepositoryImpl.deleteOfflineCache()
            _isLoading.value = false
        }
    }

}
