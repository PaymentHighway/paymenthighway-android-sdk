package io.paymenthighway.sdk.util

/**
 * Used to represent whether a sdk operation was successful or encountered an error
 *
 * @param V Type of the value returned for a successful operation
 * @param E Type of the error
 */
sealed class Result <out V, out E: Exception> {

    /**
     * Operation was successful and return a associated value of type V
     *
     * @param value associated to the sucessful operation
     */
    data class Success<out V>(val value: V) : Result<V, Nothing>()

    /**
     * Operation failed and returned an associated Exception
     *
     * @param error exception associated to the failed operation
     */
    data class Failure<out E: Exception>(val error: E) : Result<Nothing, E>()

    companion object {

        /**
         * Helper to build a Success operation
         *
         * @param value associated to the successful operation
         */
        fun <V> success (value: V): Result.Success<V> = Result.Success(value)

        /**
         * Helper to build a Failure operation
         *
         * @param error associated to the failed operation
         */
        fun <E: Exception> failure (error: E): Result.Failure<E> = Result.Failure(error)

        /**
         * Result built from the result of the function
         *
         * @param function returns the success value of the type V, otherwise throws an exception
         */
        inline fun <V> of(function: () -> V): Result<V, Exception> {
            return try {
                Success(function.invoke())
            } catch (exception: Exception) {
                failure(exception)
            }
        }
    }

    /**
     * Returns the associated value of type V if operation was successful, otherwise throws the failure exception
     */
    @Throws(Exception::class)
    fun getOrThrow(): V {
        return when (this) {
            is Success -> this.value
            is Failure -> throw this.error
        }
    }

    /**
     * Returns the associated value of type V if operation was successful, otherwise null
     */
    fun getValueOrNull(): V? {
        return when(this) {
            is Success -> this.value
            is Failure -> null
        }
    }

    /**
     * Returns the associated exception error of type E if operation encountered a failure, otherwise null
     */
    fun getErrorOrNull(): E? {
        return when(this) {
            is Success -> null
            is Failure -> this.error
        }
    }

    /**
     * Returns true if the result is a success
     */
    val isSuccess: Boolean
        get() = when(this) {
            is Success -> true
            is Failure -> false
        }

    /**
     * Returns true if the result is a failure
     */
    val isError: Boolean
        get() = !isSuccess
}
