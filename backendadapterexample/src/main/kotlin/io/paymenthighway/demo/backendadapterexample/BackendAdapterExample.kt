package io.paymenthighway.demo.backendadapterexample

import io.paymenthighway.sdk.BackendAdapter
import io.paymenthighway.sdk.exception.InternalErrorException
import io.paymenthighway.sdk.model.TransactionId
import io.paymenthighway.sdk.util.CallbackResult
import io.paymenthighway.sdk.util.CallbackResultConvert
import io.paymenthighway.sdk.util.Result


class BackendAdapterExample: BackendAdapter<TransactionToken> {

    override fun getTransactionId(completion: (Result<TransactionId, Exception>) -> Unit) {
        val api = BackendAdapterEndpointExample.create().transactionId()
        api.enqueue(CallbackResult<TransactionId>(completion))
    }

    override fun addCardCompleted(transactionId: TransactionId, completion: (Result<TransactionToken, Exception>) -> Unit) {
        val api = BackendAdapterEndpointExample.create().tokenizeTransaction(transactionId)
        api.enqueue(CallbackResultConvert(completion) {
            if (it.token.isNullOrEmpty()) {
                Result.failure(InternalErrorException(it.result?.code ?: 0, it.result?.message
                        ?: "Unknown error"))
            }
            else {
                Result.success(TransactionToken(it.token!!))
            }
        })
    }

}