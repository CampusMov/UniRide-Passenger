package com.campusmov.uniride.domain.shared.util

sealed class Resource<out T> {
    object Loading: Resource<Nothing>()
    data class Success<out T>(val data: T): Resource<T>()
    data class Failure(val message: String): Resource<Nothing>()
}