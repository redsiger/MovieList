package com.example.movielist.utils

import java.lang.Exception
import java.lang.IllegalStateException

typealias Mapper<Input, Output> = (Input) -> Output

sealed class Status<out T> {
    data class Success<out T>(val data: T): Status<T>()
    data class Error(val exception: Exception): Status<Nothing>()
    object InProgress: Status<Nothing>()
    object Empty: Status<Nothing>()

    fun <T> pass() = when(this) {
        is InProgress -> InProgress
        is Error -> Error(this.exception)
        is Empty -> Empty
        is Success -> Success(this.data)
    }

    fun <R> proceed(mapper: Mapper<T, R>? = null) = when(this) {
        is InProgress -> InProgress
        is Error -> Error(this.exception)
        is Empty -> Empty
        is Success -> {
            if (mapper == null) throw IllegalStateException("Mapper should not be NULL for success status")
            Success(mapper(this.data))
        }
    }

    val extractData: T?
        get() = when (this) {
            is Success -> data
            is Error -> null
            is InProgress -> null
            is Empty -> null
        }
}
