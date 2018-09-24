package io.paymenthighway.demo.backendadapterexample

data class TransactionCard(val cardType: String, val partialPan: String, val expireMonth: String, val expireYear: String)

data class TransactionToken(val token: String, val card: TransactionCard)