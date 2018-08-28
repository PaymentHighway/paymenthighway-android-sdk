package io.paymenthighway.sdk.model

import io.paymenthighway.sdk.CardBrand
import io.paymenthighway.sdk.util.decimalDigits
import java.util.regex.Pattern

private val Char.asInt: Int get() = this - '0'

data class CardData(val pan: String, val cvc: String, val expirationDate: ExpirationDate) {
    companion object {

        fun isCardNumberValid(cardNumber: String): Boolean {
            var checksum: Int = 0

            for (i in cardNumber.length - 1 downTo 0 step 2) {
                checksum += cardNumber.get(i).asInt
            }
            for (i in cardNumber.length - 2 downTo 0 step 2) {
                val n = cardNumber.get(i).asInt * 2
                checksum += if (n > 9) n - 9 else n
            }

            return checksum%10 == 0
        }


        fun formatCardNumber(cardNumber: String): String {
            val cardBrand = CardBrand.fromCardNumber(cardNumber) ?: return cardNumber
            val maxNumDigits = cardBrand.format.sum()
            var startIndex: Int = 0
            val list = cardBrand.format.mapNotNull {
                if (startIndex >= minOf(cardNumber.length, maxNumDigits)) { null } else {
                    val block = cardNumber.substring(startIndex, minOf(startIndex+it,cardNumber.length))
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
            val onlyDigitsSecurityCode = securityCode.decimalDigits
            return when (onlyDigitsSecurityCode.length) {
                in 0..4 -> onlyDigitsSecurityCode
                5 -> onlyDigitsSecurityCode.removeRange(4, 5)
                else -> ""
            }
        }
    }
}