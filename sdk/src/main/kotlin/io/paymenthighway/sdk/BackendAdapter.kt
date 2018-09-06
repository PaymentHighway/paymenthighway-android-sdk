package io.paymenthighway.sdk

import io.paymenthighway.sdk.model.TransactionId
import io.paymenthighway.sdk.util.Result

interface BackendAdapter<V> {

    fun getTransactionId(completion: (Result<TransactionId, Exception>) -> Unit)

    fun addCardCompleted(transactionId: TransactionId, completion: (Result<V, Exception>) -> Unit)

}