package io.paymenthighway.sdk.ui

interface ValidationListener {
    fun isValidDidChange(isValid: Boolean)
}