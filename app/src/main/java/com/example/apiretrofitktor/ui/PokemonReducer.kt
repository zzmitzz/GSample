package com.example.apiretrofitktor.ui

import android.view.View
import androidx.room.util.copy
import com.example.apiretrofitktor.base.ViewState
import com.example.apiretrofitktor.data.PokemonRepository
import com.example.apiretrofitktor.ui.model.Pokemon
import com.example.apiretrofitktor.ui.mvi.Mutation
import com.example.apiretrofitktor.ui.mvi.PokemonState
import com.example.apiretrofitktor.ui.mvi.Reducer
import javax.inject.Inject

class PokemonReducer @Inject constructor(
    val pokemonRepositoryImpl: PokemonRepository,
) : Reducer<Mutation,PokemonState>{
    override fun invoke(mutation: Mutation, currentState: PokemonState):  PokemonState {
        return when(mutation){
            Mutation.DismissError -> {
                currentState.addPokemonListError()
            }
            is Mutation.ShowContent -> {
                pokemonRepositoryImpl.getPokemon()
            }
            is Mutation.ShowError->
            Mutation.ShowLoader -> TODO()
        }
    }
    private fun PokemonState.addPokemonListError() = copy(
        loading = false,
        listPokemon = emptyList(),
        error = "Error"
    )

    private fun PokemonState.addPokemonListSuccess() = copy(
        loading = true,
    )

}