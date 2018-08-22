package io.paymenthighway.demo.backendadapterexample

import io.paymenthighway.sdk.BackendAdapter
import io.paymenthighway.sdk.exception.*
import io.paymenthighway.sdk.model.TransactionId
import io.paymenthighway.sdk.util.CallbackResult
import io.paymenthighway.sdk.util.Result
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BackendAdapterExample: BackendAdapter<TransactionToken> {

    override fun getTransactionId(completion: (Result<TransactionId, Exception>) -> Unit) {
        val api = BackendAdapterEndpointExample.create().transactionKey()
        api.enqueue(CallbackResult<TransactionId>(completion))
    }

    override fun cardAdded(transactionId: TransactionId, completion: (Result<TransactionToken, Exception>) -> Unit) {
        val api = BackendAdapterEndpointExample.create().tokenizeTransaction(transactionId)
        api.enqueue(CallbackResult<TransactionToken>(completion))
    }

}