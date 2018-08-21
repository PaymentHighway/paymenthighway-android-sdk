package io.paymenthighway.sdk.model

data class CardData(val pan: String, val cvc: String, val expirationDate: ExpirationDate)