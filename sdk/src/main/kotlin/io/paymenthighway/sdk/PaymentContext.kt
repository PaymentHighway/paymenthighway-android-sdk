package io.paymenthighway.sdk

import io.paymenthighway.sdk.exception.InternalErrorException
import io.paymenthighway.sdk.model.*
import io.paymenthighway.sdk.service.PaymentHighwayService
import io.paymenthighway.sdk.util.Result

/**
 * PaymentContext manages all the functionality the payment context, such as adding a new payment card.
 *
 * @param V the type of the BackendAdapter
 * @param [config] the configuration for Payment Highway
 * @param [backendAdapter] provide your BackendAdapter implementation
 * @constructor Main constructor
 *
 */
class PaymentContext<V>(config: PaymentConfig, private val backendAdapter: BackendAdapter<V>) {

    private val service: PaymentHighwayService

    init {
        service = PaymentHighwayService(config.merchantId, config.accountId)
    }

    /**
     * Adds a new Payment Card
     *
     * @param card Card to be added
     * @param completion Callback closure with the result of the operation
     *
     * @see Result
     *
     */
    fun addCard(card: CardData, completion: (Result<V, Exception>) -> Unit) {
        backendAdapter.getTransactionId { result ->
            when (result) {
                is Result.Success -> getTransactionIdHandler(result.value, card, completion)
                is Result.Failure -> completion(result)
            }
        }
    }

    private fun getTransactionIdHandler(transactionId: TransactionId, card: CardData, completion: (Result<V, Exception>) -> Unit ) {
        service.encryptionKey(transactionId) { result ->
            when (result) {
                is Result.Success -> getEncryptionKeyHandler(result.value, transactionId, card, completion)
                is Result.Failure -> completion(result)
            }
        }
    }

    private fun getEncryptionKeyHandler(encryptionKey: EncryptionKey, transactionId: TransactionId, card: CardData, completion: (Result<V, Exception>) -> Unit ) {
        service.tokenizeTransaction(transactionId, card, encryptionKey) { result ->
            when (result) {
                is Result.Success -> {
                    backendAdapter.addCardCompleted(transactionId, completion)
                }
                is Result.Failure -> completion(result)
            }
        }
    }
}