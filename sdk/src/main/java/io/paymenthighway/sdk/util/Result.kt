package io.paymenthighway.sdk.util

sealed class Result <out V, out E: Exception> {

    data class Success<out V>(val value: V) : Result<V, Nothing>()

    data class Failure<out E: Exception>(val error: E) : Result<Nothing, E>()

    companion object {

        fun <V> success (value: V): Result.Success<V> = Result.Success(value)

        fun <E: Exception> failure (error: E): Result.Failure<E> = Result.Failure(error)

        inline fun <V> of(function: () -> V): Result<V, Exception> {
            return try {
                Success(function.invoke())
            } catch (exception: Exception) {
                failure(exception)
            }
        }
    }

    @Throws(Exception::class)
    fun getOrThrow(): V {
        return when (this) {
            is Success -> this.value
            is Failure -> throw this.error
        }
    }

    fun getRawValue(): V? {
        return when(this) {
            is Success -> this.value
            is Failure -> null
        }
    }

    fun getRawError(): E? {
        return when(this) {
            is Success -> null
            is Failure -> this.error
        }
    }

    val isValue: Boolean
        get() = when(this) {
            is Success -> true
            is Failure -> false
        }

    val isError: Boolean
        get() = !isValue
}
