package com.example.apiretrofitktor.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apiretrofitktor.data.PokemonRepository
import com.example.apiretrofitktor.data.ServiceLocatorAPI
import com.example.apiretrofitktor.data.remote.model.Response
import com.example.apiretrofitktor.data.remote.NetworkService
import com.example.apiretrofitktor.data.remote.model.Pokemon
import kotlinx.coroutines.launch

class PokemonViewModel(
    private val pokemonRepository: PokemonRepository,
) : ViewModel() {
    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    var isLoadMore: MutableLiveData<Boolean> = MutableLiveData(false)

    val listPokemon: MutableLiveData<MutableList<Pokemon>> = MutableLiveData<MutableList<Pokemon>>()


    fun getPokemon(limit: Int, offset: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            listPokemon.value = pokemonRepository.getPokemonRemote(limit,offset).toMutableList()
            pokemonRepository.insertAllPokemon(listPokemon.value!!.map {
                it.toPokemonItem())
        }
            _isLoading.value = false
        }
    }
    fun getOfflinePokemon() {
        viewModelScope.launch {
            listPokemon.value = pokemonRepository.getPokemonLocal()
        }
    }
}
