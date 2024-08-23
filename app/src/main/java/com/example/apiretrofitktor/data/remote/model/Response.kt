package com.example.apiretrofitktor.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class Response(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<Pokemon>
)