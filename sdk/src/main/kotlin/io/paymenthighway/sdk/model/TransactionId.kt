package io.paymenthighway.sdk.model

/**
 * Data class to hold transaction id
 *
 * @param id Actual transaction id as String
 */
data class TransactionId(val id: String) {

    /**
     * Return the actual string value representation the object
     */
    override fun toString(): String {
        return id
    }
}