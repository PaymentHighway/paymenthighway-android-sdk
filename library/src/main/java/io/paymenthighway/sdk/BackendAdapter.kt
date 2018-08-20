package io.paymenthighway.sdk

import io.paymenthighway.sdk.model.TransactionId
import io.paymenthighway.sdk.util.Result

interface BackendAdapter<V, E> {

    fun getTransactionId(completion: (Result<TransactionId, E>) -> Unit)

    fun cardAdded(transactionId: TransactionId, completion: (Result<V, E>) -> Unit)
}