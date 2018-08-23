package io.paymenthighway.sdk

//internal interface Properties {
//    val baseURL: String
//}

internal class PaymentHighwayProperties {
    companion object {
        var baseURL: String = BuildConfig.BASE_URL
    }
 }