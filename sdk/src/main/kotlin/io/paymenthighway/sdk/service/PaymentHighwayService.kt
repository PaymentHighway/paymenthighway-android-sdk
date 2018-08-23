package io.paymenthighway.sdk.service

import io.paymenthighway.sdk.exception.InternalErrorException
import io.paymenthighway.sdk.exception.*
import io.paymenthighway.sdk.model.*
import io.paymenthighway.sdk.util.CallbackResult
import io.paymenthighway.sdk.util.CallbackResultConvert
import io.paymenthighway.sdk.util.Result
import io.paymenthighway.sdk.util.tokenizeCardData

internal class PaymentHighwayService(val merchantId: MerchantId, val accountId: AccountId) {

    fun transactionKey(transactionId: TransactionId, completion: (Result<TransactionKey, Exception>) -> Unit) {

        val api = PaymentHighwayEndpoint.create(merchantId, accountId).transactionKey(transactionId)

        api.enqueue(CallbackResultConvert(completion) {
            if (it.key.isNullOrEmpty()) Result.failure(InternalErrorException(it.result?.code
                    ?: 0, it.result?.message ?: "Unknown error")) else Result.success(TransactionKey(it.key!!))
        })
    }

    fun tokenizeTransaction(
        transactionId: TransactionId,
        cardData: CardData,
        transactionKey: TransactionKey,
        completion: (Result<ApiResult, Exception>) -> Unit) {

        val tokenizeCardDataResult = tokenizeCardData(cardData, transactionKey)
        val tokenizeCardData = try { tokenizeCardDataResult.getOrThrow() } catch (exception: Exception) {  completion(Result.failure(tokenizeCardDataResult.getRawError()!!))
            return
        }

        val api = PaymentHighwayEndpoint.create(merchantId, accountId).tokenizeTransaction(transactionId, tokenizeCardData)
        api.enqueue(CallbackResult(completion))
    }
}
