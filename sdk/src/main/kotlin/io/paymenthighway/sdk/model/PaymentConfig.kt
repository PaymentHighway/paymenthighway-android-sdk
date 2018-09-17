package io.paymenthighway.sdk.model

/**
 * Payment Highway configuration
 *
 * @param merchantId Merchant identifier
 * @param accountId Account identifier
 *
 */
data class PaymentConfig(val merchantId: MerchantId, val accountId: AccountId)
