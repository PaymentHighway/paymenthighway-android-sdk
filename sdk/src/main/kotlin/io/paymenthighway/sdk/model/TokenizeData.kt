package io.paymenthighway.sdk.model

internal data class TokenizeData(val encrypted: String, val key: TokenizeDataKey)
internal data class TokenizeDataKey(val key: String, val iv: String)