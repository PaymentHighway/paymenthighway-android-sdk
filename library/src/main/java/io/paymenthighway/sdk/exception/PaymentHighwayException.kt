package io.paymenthighway.sdk.exception

class InvalidDataException(message: String) : Exception(message)
class UnknowErrorException(message: String = "") : Exception(message)
class NetworkErrorException(message: String, cause: Throwable) : Exception(message, cause)
class InternalErrorException(code: Int, message: String) : Exception("Error code: $code - $message")
