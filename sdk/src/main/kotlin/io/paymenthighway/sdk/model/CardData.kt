package io.paymenthighway.sdk.model

import io.paymenthighway.sdk.CardBrand
import io.paymenthighway.sdk.util.decimalDigits

private val Char.asInt: Int get() = this - '0'

data class CardData(val pan: String, val cvc: String, val expirationDate: ExpirationDate) {
    companion object {

        fun isCardNumberValid(cardNumber: String): Boolean {

            val cardNumberDigits = cardNumber.decimalDigits

            val cardBrand = CardBrand.fromCardNumber(cardNumberDigits) ?: return false
            if (!cardBrand.panLength.contains(cardNumberDigits.length)) return false

            var checksum: Int = 0

            for (i in cardNumberDigits.length - 1 downTo 0 step 2) {
                checksum += cardNumberDigits.get(i).asInt
            }
            for (i in cardNumberDigits.length - 2 downTo 0 step 2) {
                val n = cardNumberDigits.get(i).asInt * 2
                checksum += if (n > 9) n - 9 else n
            }

            return checksum%10 == 0
        }


        fun formatCardNumber(cardNumber: String): String {
            val cardNumberDigits = cardNumber.decimalDigits
            val cardBrand = CardBrand.fromCardNumber(cardNumber) ?: return cardNumber
            val maxNumDigits = cardBrand.format.sum()
            var startIndex: Int = 0
            val list = cardBrand.format.mapNotNull {
                if (startIndex >= minOf(cardNumberDigits.length, maxNumDigits)) { null } else {
                    val block = cardNumberDigits.substring(startIndex, minOf(startIndex+it, cardNumberDigits.length))
                    startIndex+= it
                    block
                }
            }
            return list.joinToString(" ")
        }

        fun isSecurityCodeValid(securityCode: String, cardBrand: CardBrand?): Boolean {
            if (cardBrand == null) return false
            return cardBrand.cvcLength.contains(securityCode.decimalDigits.length)
        }

        fun formatSecurityCode(securityCode: String): String {
            val securityCodeDigits = securityCode.decimalDigits
            return when (securityCodeDigits.length) {
                in 0..4 -> securityCodeDigits
                5 -> securityCodeDigits.removeRange(4, 5)
                else -> ""
            }
        }
    }
}