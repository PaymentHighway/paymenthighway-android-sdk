package io.paymenthighway.sdk.exception

class InvalidDataException(message: String) : Exception(message)
class UnknowErrorException(message: String = "") : Exception(message)
class InternalErrorException(code: Int, message: String) : Exception("Error code: $code - $message")
class ResponseErrorException(statusCode: Int, message: String) : Exception("$statusCode - $message")
