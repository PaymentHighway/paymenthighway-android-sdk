package io.paymenthighway.sdk.model

import io.paymenthighway.sdk.EnvironmentInterface

/**
 * Payment Highway configuration
 *
 * @param merchantId Merchant identifier
 * @param accountId Account identifier
 * @param environment Payment Highway environment
 *
 */
data class PaymentConfig(val merchantId: MerchantId, val accountId: AccountId, val environment: EnvironmentInterface)
