package com.example.apiretrofitktor.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apiretrofitktor.base.ViewState
import com.example.apiretrofitktor.data.PokemonRepository
import com.example.apiretrofitktor.data.ResultOf
import com.example.apiretrofitktor.ui.mvi.Mutation
import com.example.apiretrofitktor.ui.mvi.PokemonIntent
import com.example.apiretrofitktor.ui.mvi.PokemonState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PokemonViewModel @Inject constructor(
    val pokemonRepositoryImpl: PokemonRepository
) : ViewModel() {

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isLoadMore: Boolean = false


    private val _state = MutableStateFlow(PokemonState())
    val state
        get() = _state

    fun handleIntent(intent: PokemonIntent) {
        viewModelScope.launch(Dispatchers.IO) {
            when (intent) {
                is PokemonIntent.LoadingPokemon -> {
                    _state.update { _state.value.copy(loading = true) }
                    val result = pokemonRepositoryImpl.getPokemon(
                        intent.network,
                        intent.limit,
                        intent.offset
                    )
                    val newState = when (result) {
                        is ResultOf.Success -> {
                            _state.value.copy(
                                loading = false,
                                listPokemon = result.data,
                                error = null
                            )
                        }

                        is ResultOf.Error -> {
                            _state.value.copy(
                                loading = false,
                                listPokemon = emptyList(),
                                error = result.message
                            )
                        }

                    }
                    isLoadMore = false
                    _state.update { newState }
                }

                is PokemonIntent.DeleteDb -> {
                    nukeDB()
                }
            }
        }
    }

    private suspend fun nukeDB() {
        viewModelScope.launch {
            _isLoading.value = true
            pokemonRepositoryImpl.deleteOfflineCache()
            _isLoading.value = false
        }
    }

}
