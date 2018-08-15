package io.paymenthighway.sdk

class PaymentContext(val config: PaymentConfig) {

    fun getMerchantId(): String = config.merchantId
}