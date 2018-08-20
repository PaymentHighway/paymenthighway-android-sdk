package io.paymenthighway.sdk.model

data class ApiResult(val result: ApiResultInfo?)
data class ApiResultInfo(val code: Int, val message: String)