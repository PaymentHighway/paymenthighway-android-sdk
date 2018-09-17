package io.paymenthighway.sdk.util

/**
 * Used to represent whether a sdk operation was successful or encountered an error
 *
 * @param V Type of the value in returned for a successful operation
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
     * Operation failed and return a associated Exception
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
         * @param function function return the success value of type V otherwise throws a exception
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
     * Return the associated value of type V if operation were successful otherwise throws the failure exception
     */
    @Throws(Exception::class)
    fun getOrThrow(): V {
        return when (this) {
            is Success -> this.value
            is Failure -> throw this.error
        }
    }

    /**
     * Return the associated value of type V if operation were successful otherwise null
     */
    fun getValueOrNull(): V? {
        return when(this) {
            is Success -> this.value
            is Failure -> null
        }
    }

    /**
     * Return the associated exceprion error of type E if operation encountered a failure otherwise null
     */
    fun getErrorOrNull(): E? {
        return when(this) {
            is Success -> null
            is Failure -> this.error
        }
    }

    /**
     * Return true if the result is a success
     */
    val isValue: Boolean
        get() = when(this) {
            is Success -> true
            is Failure -> false
        }

    /**
     * Return true if the result is a failure
     */
    val isError: Boolean
        get() = !isValue
}
