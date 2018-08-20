package io.paymenthighway.sdk

import io.paymenthighway.sdk.model.PaymentConfig

class PaymentContext(val config: PaymentConfig) {

    fun getMerchantId(): String = config.merchantId
}