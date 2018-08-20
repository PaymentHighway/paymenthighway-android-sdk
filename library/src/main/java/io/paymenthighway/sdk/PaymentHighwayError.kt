package io.paymenthighway.sdk

sealed class PaymentHighwayError {
    data class invalidURL(val url: String): PaymentHighwayError ()
}