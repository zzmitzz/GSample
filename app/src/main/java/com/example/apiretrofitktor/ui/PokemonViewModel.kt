package com.example.apiretrofitktor.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apiretrofitktor.ServiceLocatorAPI
import com.example.apiretrofitktor.data.model.Response
import com.example.apiretrofitktor.data.remote.NetworkService
import kotlinx.coroutines.launch

class PokemonViewModel(
    private val networkService: NetworkService,
) : ViewModel() {
    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    var isLoadMore: MutableLiveData<Boolean> = MutableLiveData(false)

    val response: MutableLiveData<Response> = MutableLiveData<Response>()
    var cachePokemon : MutableLiveData<Response> = MutableLiveData()
    val ktorClient = ServiceLocatorAPI.initKtorClient()
    fun getPokemon(limit: Int, offset: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            response.value = ktorClient.getPokemonList(limit,offset)
            _isLoading.value = false
        }
    }
    fun loadMorePokemon(limit: Int, offset: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            cachePokemon.value = networkService.getPokemonList(limit, offset)
            isLoadMore.value = false
            _isLoading.value = false
        }
    }
}
