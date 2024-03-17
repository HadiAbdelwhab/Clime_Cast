package com.example.climecast.network

sealed class ApiState<out T> {
    data class Success<out T>(val data: T) : ApiState<T>()
    data class Error(val exception: Throwable) : ApiState<Nothing>()
    data object Loading : ApiState<Nothing>()
}