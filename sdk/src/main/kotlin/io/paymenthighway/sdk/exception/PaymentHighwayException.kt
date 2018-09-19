package io.paymenthighway.sdk.exception

/**
 * Data received from REST api are invalid
 */
class InvalidDataException(message: String) : Exception(message)

/**
 * Data received from REST api are empty
 */
class EmptyDataException() : Exception("Empty data!")

/**
 * Returned when the source of the error is unknown
 */
class UnknowErrorException(message: String = "") : Exception(message)

/**
 * Error returned from the Payment Highway REST api
 */
class InternalErrorException(code: Int, message: String) : Exception("Error code: $code - $message")

/**
 * REST api response error
 */
class ResponseErrorException(statusCode: Int, message: String) : Exception("$statusCode - $message")
