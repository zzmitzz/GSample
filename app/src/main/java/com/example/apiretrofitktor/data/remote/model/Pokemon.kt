package com.example.apiretrofitktor.data.remote.model

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.bumptech.glide.Glide
import com.example.apiretrofitktor.data.local.entity.PokemonEntity
import com.example.apiretrofitktor.ui.model.PokemonItem
import kotlinx.serialization.Serializable
import java.io.ByteArrayOutputStream


@Serializable
data class Pokemon(
    val name: String,
    val url: String
)

fun Pokemon.toPokemonItem() : PokemonItem {
    val id = url.split("/".toRegex()).dropLast(1).last().toInt()
    Log.d("TAG", id.toString())
    return PokemonItem(
        image = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png",
        name = name,
        id = id
    )
}

fun Pokemon.toPokemonEntity(context: Context) : PokemonEntity {
    return PokemonEntity(
        id = url.split("/".toRegex()).dropLast(1).last().toInt(),
        name = name,
        image = Glide.with(context)
            .asBitmap()
            .load(imageUrl)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    val byteArray = bitmapToByteArray(resource)
                    callback(byteArray)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    // Handle placeholder if needed
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    super.onLoadFailed(errorDrawable)
                    callback(null)  // Handle failure
                }
            })
    )
}

private fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
    val byteArrayOutputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
    return byteArrayOutputStream.toByteArray()
}