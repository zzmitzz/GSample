package com.example.apiretrofitktor.data


sealed class ResultOf<out T> {
    data class Success<out R>(val data: R) : ResultOf<R>()
    data class Error(val message: String, val exception: Exception) : ResultOf<Nothing>()
}