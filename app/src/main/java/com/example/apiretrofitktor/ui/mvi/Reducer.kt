package com.example.apiretrofitktor.ui.mvi


interface Reducer <Mutation, ViewState>{
    operator fun invoke(mutation: Mutation, viewState: ViewState): ViewState
}