package io.paymenthighway.sdk.model

import io.paymenthighway.sdk.CardBrand
import io.paymenthighway.sdk.util.decimalDigits

private val Char.asInt: Int get() = this - '0'

/**
 * Data class to hold card info
 *
 * @param pan The card number
 * @param cvc The card security code
 * @param expiryDate The card expiry date
 */

data class CardData(val pan: String, val cvc: String, val expiryDate: ExpiryDate) {
    companion object {

        /**
         * Validate a given credit card number using the Luhn algorithm
         *
         * @param cardNumber The card number to validate
         * @return true if the credit card is valid
         */
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

        /**
         * Format a given card number to a neat string
         *
         * @param cardNumber The card number to format
         * @return The formatted credit card number properly spaced
         */
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

        /**
         * Validate a given security code
         * Need the card brand to understand the lenght of the security code
         *
         * @param securityCode The security code to validate
         * @return true if the security code is valid
         */
        fun isSecurityCodeValid(securityCode: String, cardBrand: CardBrand?): Boolean {
            if (cardBrand == null) return false
            return cardBrand.cvcLength.contains(securityCode.decimalDigits.length)
        }

        /**
         * Format a given security code
         *
         * @param securityCode The security code to format
         * @return The formatted security code
         */
        fun formatSecurityCode(securityCode: String, cardBrand: CardBrand?): String {
            val securityCodeDigits = securityCode.decimalDigits
            if (cardBrand == null) {
                return when (securityCodeDigits.length) {
                    in 0..4 -> securityCodeDigits
                    5 -> securityCodeDigits.removeRange(4, 5)
                    else -> ""
                }
            } else {
                return when {
                    securityCodeDigits.length <= cardBrand.cvcLength.max()!! -> securityCodeDigits
                    else -> securityCodeDigits.removeRange(cardBrand.cvcLength.max()!!,  securityCodeDigits.length)
                }
            }
        }
    }
}