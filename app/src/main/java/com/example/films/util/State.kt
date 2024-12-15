package com.example.films.util

sealed class State<T>(val data: T? = null, val message: String? = null) {
    class Error<T>(message: String, data: T?=null) : State<T>( data, message)
    class Success<T>(data: T?) : State<T>(data)
    class Loading<T> : State<T>()
}