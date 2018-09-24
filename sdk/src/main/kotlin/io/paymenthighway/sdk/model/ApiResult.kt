package io.paymenthighway.sdk.model


/**
 * Json mapping of the result data from the Payment Highway API
 *
 * @param result Code value and message result
 */
data class ApiResult(override val result: ApiResultInfo): ApiResultInterface {
    companion object {
        /**
         * Code value when the api result is successful
         */
        val OK = 100
    }
}

/**
 * Result info from Payment Highway API
 *
 * @param code Error code
 * @param message Error message
 */
data class ApiResultInfo(val code: Int, val message: String)

interface ApiResultInterface {
    val result: ApiResultInfo
}
