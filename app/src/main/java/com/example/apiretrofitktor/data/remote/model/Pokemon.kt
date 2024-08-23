package com.example.apiretrofitktor.data.remote.model

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.apiretrofitktor.R
import com.example.apiretrofitktor.data.local.entity.PokemonEntity
import com.example.apiretrofitktor.ui.model.PokemonItem
import kotlinx.serialization.Serializable
import java.io.ByteArrayOutputStream


@Serializable
data class Pokemon(
    val name: String,
    val url: String
)

fun Pokemon.toPokemonItem(image: Bitmap?) : PokemonItem {
    val id = url.split("/".toRegex()).dropLast(1).last().toInt()
    Log.d("TAG", id.toString())
    if(image != null){
         return PokemonItem(
            image = image,
            name = name,
            id = id
        )
    }
    return PokemonItem(
        image = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888),
        name = name,
        id = id
    )
}


private fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
    val byteArrayOutputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
    return byteArrayOutputStream.toByteArray()
}