package io.paymenthighway.sdk.model


data class ApiResult(val result: ApiResultInfo) {
    companion object {
        val OK = 100
    }
}
data class ApiResultInfo(val code: Int, val message: String)