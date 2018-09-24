package io.paymenthighway.sdk

import io.paymenthighway.sdk.model.TransactionId
import io.paymenthighway.sdk.util.Result

/**
 * Backend Adapter
 *
 * This interface defines what customer has to implement (in own backend) in order to use Payment Highway API
 *
 * @param T the type of the return data for addCardCompleted
 */
interface BackendAdapter<V> {

    /**
     * Get the transaction id
     *
     * Fetch Payment Highway transaction ID via the merchant’s own backend.
     * The merchant backend gets the ID from Payment Highway using the Init Transaction call.
     */
    fun getTransactionId(completion: (Result<TransactionId, Exception>) -> Unit)

    /**
     * Add card completed
     *
     * Callback function, which is called once the add card operation has been completed by the user.
     * Here one implements the call to the merchant’s backend, which then fetches the actual tokenization result, the card token and its details from Payment Highway using the provided transaction/tokenization ID ( https://dev.paymenthighway.io/#tokenization ) and stores them
     * Typically the response from the merchant’s backend consists of the card details to display to the user and an identifier for the card, e.g. the card token, or some merchant’s internal ID for this payment method.
     */
    fun addCardCompleted(transactionId: TransactionId, completion: (Result<V, Exception>) -> Unit)

}