package io.paymenthighway.sdk.util

sealed class Result <out V, out E> {

    data class Success<out V>(val value: V) : Result<V, Nothing>()

    data class Failure<out E>(val error: E) : Result<Nothing, E>()

    companion object {
        fun <V> success (value: V): Result.Success<V> = Result.Success(value)
        fun <E> failure (error: E): Result.Failure<E> = Result.Failure(error)
    }
}