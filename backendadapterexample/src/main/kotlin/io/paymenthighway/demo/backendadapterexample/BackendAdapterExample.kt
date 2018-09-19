package io.paymenthighway.demo.backendadapterexample

import io.paymenthighway.sdk.BackendAdapter
import io.paymenthighway.sdk.exception.EmptyDataException
import io.paymenthighway.sdk.exception.InternalErrorException
import io.paymenthighway.sdk.exception.InvalidDataException
import io.paymenthighway.sdk.model.TransactionId
import io.paymenthighway.sdk.util.CallbackResult
import io.paymenthighway.sdk.util.CallbackResultConvert
import io.paymenthighway.sdk.util.Result


class BackendAdapterExample: BackendAdapter<TransactionToken> {

    override fun getTransactionId(completion: (Result<TransactionId, Exception>) -> Unit) {
        val api = BackendAdapterEndpointExample.create().transactionId()
        api.enqueue(CallbackResult(completion))
    }

    override fun addCardCompleted(transactionId: TransactionId, completion: (Result<TransactionToken, Exception>) -> Unit) {
        val api = BackendAdapterEndpointExample.create().tokenizeTransaction(transactionId)
        api.enqueue(CallbackResultConvert(completion) {
            val token = it.token?.let { it } ?: return@CallbackResultConvert Result.failure(EmptyDataException())
            val card = it.card?.let { it } ?: return@CallbackResultConvert Result.failure(EmptyDataException())
            Result.success(TransactionToken(token, card))
        })
    }

}