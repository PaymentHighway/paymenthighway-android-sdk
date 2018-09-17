package io.paymenthighway.sdk

import io.paymenthighway.sdk.exception.InternalErrorException
import io.paymenthighway.sdk.model.*
import io.paymenthighway.sdk.service.PaymentHighwayService
import io.paymenthighway.sdk.util.Result

/**
 * PaymentContext manage all the functionality around a payment like addCard
 *
 * @param V the type of the BackendAdapter
 * @constructor Main constructor
 * @param [config] the configuration for Payment Highway
 * @param [backendAdapter] provide your BackendAdapter implementation
 *
 */
class PaymentContext<V>(config: PaymentConfig, private val backendAdapter: BackendAdapter<V>) {

    private val service: PaymentHighwayService

    init {
        service = PaymentHighwayService(config.merchantId, config.accountId)
    }

    /**
     * This add a new Payment Card
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
                is Result.Failure -> completion(Result.failure(result.error))
            }
        }
    }

    private fun getTransactionIdHandler(transactionId: TransactionId, card: CardData, completion: (Result<V, Exception>) -> Unit ) {
        service.encryptionKey(transactionId) { result ->
            when (result) {
                is Result.Success -> getEncryptionKeyHandler(result.value, transactionId, card, completion)
                is Result.Failure -> completion(Result.failure(result.error))
            }
        }
    }

    private fun getEncryptionKeyHandler(encryptionKey: EncryptionKey, transactionId: TransactionId, card: CardData, completion: (Result<V, Exception>) -> Unit ) {
        service.tokenizeTransaction(transactionId, card, encryptionKey) { result ->
            when (result) {
                is Result.Success -> {
                    val apiResult = result.value
                    if (apiResult.result.code == ApiResult.OK) {
                        backendAdapter.addCardCompleted(transactionId, completion)
                    }
                    else {
                        completion(Result.failure(InternalErrorException(apiResult.result.code, apiResult.result.message)))
                    }
                }
                is Result.Failure -> completion(Result.failure(result.error))
            }
        }
    }
}