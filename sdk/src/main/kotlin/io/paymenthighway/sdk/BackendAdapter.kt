package io.paymenthighway.sdk

import io.paymenthighway.sdk.model.TransactionId
import io.paymenthighway.sdk.util.Result

/**
 * Backend Adapter
 *
 * This interface define what customer have to implement (in own backend) in order to use Payment Highway API
 *
 * @param T the type of the return data for addCardCompleted
 */
interface BackendAdapter<V> {

    /**
     * Get the transaction id
     *
     * Customer need to implement REST API to get from own backend a transaction id
     */
    fun getTransactionId(completion: (Result<TransactionId, Exception>) -> Unit)

    /**
     * Add card completed
     *
     * Customer need to implement REST API to notify to own backend that operation to add card is completed.
     * Backend might return data in order to perform a payment like a transaction token
     */
    fun addCardCompleted(transactionId: TransactionId, completion: (Result<V, Exception>) -> Unit)

}