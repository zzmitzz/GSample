package com.example.apiretrofitktor.ui.model

import android.graphics.Bitmap
import com.example.apiretrofitktor.data.local.entity.PokemonEntity
import java.io.ByteArrayOutputStream

data class Pokemon (
    val image: Bitmap,
    val name: String,
    val id: Int
)

fun Pokemon.toPokemonEntity(): PokemonEntity {
    return PokemonEntity(
        id = id,
        name = name,
        image = bitmapToByteArray(image)
    )
}

fun bitmapToByteArray(image: Bitmap): ByteArray {
    val stream = ByteArrayOutputStream()
    image.compress(Bitmap.CompressFormat.PNG, 100, stream)
    return stream.toByteArray()
}
