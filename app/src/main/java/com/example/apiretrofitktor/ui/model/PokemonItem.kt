package com.example.apiretrofitktor.ui.model

import android.graphics.Bitmap
import com.example.apiretrofitktor.data.local.entity.PokemonEntity
import java.io.ByteArrayOutputStream

data class PokemonItem (
    val image: Bitmap,
    val name: String,
    val id: Int
)

fun PokemonItem.toPokemonEntity(): PokemonEntity {
    return PokemonEntity(
        name = name,
        image = bitmapToByteArray(image)
    )
}

fun bitmapToByteArray(image: Bitmap): ByteArray {
    val stream = ByteArrayOutputStream()
    image.compress(Bitmap.CompressFormat.PNG, 100, stream)
    return stream.toByteArray()
}
