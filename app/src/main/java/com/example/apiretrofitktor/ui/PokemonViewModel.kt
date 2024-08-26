package com.example.apiretrofitktor.ui

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apiretrofitktor.data.PokemonRepository
import com.example.apiretrofitktor.data.local.entity.toPokemonItem
import com.example.apiretrofitktor.data.remote.model.toPokemonItem
import com.example.apiretrofitktor.ui.model.PokemonItem
import com.example.apiretrofitktor.ui.model.toPokemonEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL
import javax.inject.Inject


@HiltViewModel
class PokemonViewModel @Inject constructor(
    val pokemonRepository: PokemonRepository
) : ViewModel() {

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    var isLoadMore: MutableLiveData<Boolean> = MutableLiveData(false)

    val listPokemon: MutableLiveData<MutableList<PokemonItem>> =
        MutableLiveData<MutableList<PokemonItem>>()

    fun getRemotePokemon(
        limit: Int,
        offset: Int,
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            listPokemon.value =
                pokemonRepository
                    .getPokemonRemote(limit, offset)
                    .map { it ->
                        val id =
                            it.url
                                .split("/".toRegex())
                                .dropLast(1)
                                .last()
                                .toInt()
                        it.toPokemonItem(UrlToBitmap(id))
                    }.toMutableList()
            isLoadMore.value = false
            _isLoading.value = false
            pokemonRepository.insertAllPokemon(listPokemon.value!!.map { it->
                it.toPokemonEntity()
            }.toList())
        }
    }

    fun getOfflinePokemon() {
        viewModelScope.launch {
            listPokemon.value = pokemonRepository.getPokemonLocal().map {
                it.toPokemonItem()
            }.toMutableList()
        }
    }
    fun NukeDB(){
        viewModelScope.launch {
            _isLoading.value = true
            pokemonRepository.deleteRoomDb()
            _isLoading.value = false
        }
    }
    suspend fun UrlToBitmap(id: Int): Bitmap? =
        withContext(Dispatchers.IO){
            try {
                val url = URL("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png")

                val bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream())
                Log.d("TAG", bitmap.toString())
                bitmap
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("TAG", e.toString())
                null
            }
        }
}
