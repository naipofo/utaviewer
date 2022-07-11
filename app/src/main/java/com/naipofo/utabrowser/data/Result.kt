package com.naipofo.utabrowser.data

sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}

inline fun <T> tryResult(pack: () -> T): Result<T> = try {
    Result.Success(pack())
} catch (e: Exception) {
    Result.Error(e)
}

fun <T> Result<T>.dataOrNull(): T? = when (this) {
    is Result.Error -> null
    is Result.Success -> this.data
}