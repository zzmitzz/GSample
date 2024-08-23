package com.example.apiretrofitktor.data.remote

import com.example.apiretrofitktor.data.remote.model.Pokemon
import com.example.apiretrofitktor.data.remote.model.Response
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface NetworkService {
    suspend fun getPokemonList(limit: Int, offset: Int): Response
}
class NetworkDataSource(
    private val networkService: NetworkService,
    private var ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun getPokemonList(limit: Int, offset: Int): List<Pokemon> {
        return withContext(ioDispatcher) {
            networkService.getPokemonList(limit, offset).results
        }
    }
}