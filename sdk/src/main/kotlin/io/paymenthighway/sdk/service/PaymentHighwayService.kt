package io.paymenthighway.sdk.service

import io.paymenthighway.sdk.exception.InternalErrorException
import io.paymenthighway.sdk.model.*
import io.paymenthighway.sdk.util.CallbackResult
import io.paymenthighway.sdk.util.CallbackResultConvert
import io.paymenthighway.sdk.util.Result
import io.paymenthighway.sdk.util.tokenizeCardData

internal class PaymentHighwayService(val merchantId: MerchantId, val accountId: AccountId) {

    fun encryptionKey(transactionId: TransactionId, completion: (Result<EncryptionKey, Exception>) -> Unit) {

        val api = PaymentHighwayEndpoint.create(merchantId, accountId).encryptionKey(transactionId)

        api.enqueue(CallbackResultConvert(completion) {
            if (it.key.isNullOrEmpty()) {
                Result.failure(InternalErrorException(it.result?.code ?: 0, it.result?.message ?: "Unknown error"))

            } else {
                Result.success(EncryptionKey(it.key!!))
            }
        })
    }

    fun tokenizeTransaction(
            transactionId: TransactionId,
            cardData: CardData,
            encryptionKey: EncryptionKey,
            completion: (Result<ApiResult, Exception>) -> Unit) {

        val tokenizeCardDataResult = tokenizeCardData(cardData, encryptionKey)
        when (tokenizeCardDataResult){
            is Result.Success -> {
                val api = PaymentHighwayEndpoint.create(merchantId, accountId).tokenizeTransaction(transactionId, tokenizeCardDataResult.value)
                api.enqueue(CallbackResult(completion))
            }
            is Result.Failure -> {
                completion(Result.failure(tokenizeCardDataResult.error))
            }
        }
    }
}
