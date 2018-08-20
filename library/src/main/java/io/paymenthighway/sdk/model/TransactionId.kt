package io.paymenthighway.sdk.model

    data class TransactionId(val id: String) {
        override fun toString(): String {
            return id
        }
    }