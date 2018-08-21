package io.paymenthighway.sdk

internal interface Properties {
    val baseURL: String
}

internal class PaymentHighwayProperties {
    companion object: Properties {
        override val baseURL: String get() = BuildConfig.BASE_URL
    }
 }