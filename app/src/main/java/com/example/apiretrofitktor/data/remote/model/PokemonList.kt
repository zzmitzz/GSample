package com.example.apiretrofitktor.data.remote.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.example.apiretrofitktor.ui.model.Pokemon
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import java.io.ByteArrayOutputStream
import java.net.URL


@Serializable
data class PokemonList(
    val name: String,
    val url: String
)

suspend fun PokemonList.toPokemon(): Pokemon {
    val id = url.split("/".toRegex()).dropLast(1).last().toInt()
    val image = urlToBitmap(id)
    if (image != null) {
        return Pokemon(
            image = image,
            name = name,
            id = id
        )
    }
    return Pokemon(
        image = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888),
        name = name,
        id = id
    )
}

suspend fun urlToBitmap(id: Int): Bitmap? =
    withContext(Dispatchers.IO) {
        try {
            val url =
                URL("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png")
            val bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream())
            Log.d("TAG", bitmap.toString())
            bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("TAG", e.toString())
            null
        }
    }

private fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
    val byteArrayOutputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
    return byteArrayOutputStream.toByteArray()
}