package com.example.apiretrofitktor.ui.mvi

import com.example.apiretrofitktor.ui.model.Pokemon

sealed interface Mutation{
    data object ShowLoader: Mutation
    data class ShowContent(val network: Boolean, val offset: Int, val limit: Int) : Mutation
    data class ShowError (val message: String): Mutation
    data object DismissError: Mutation
}