package io.paymenthighway.sdk

import io.paymenthighway.sdk.util.decimalDigits

internal interface CardBrandData {
    val panLength: IntArray
    val cvcLength: IntArray
    val pattern: String
    val format: Array<Int>
    val description: String
}

/**
 * Card brands to which a payment card can belong
 *
 */
sealed class CardBrand: CardBrandData {

    /**
     * Visa card
     */
    object visa: CardBrand()

    /**
     * Mastercard card
     */
    object mastercard: CardBrand()

    /**
     * American Express card
     */
    object americanExpress: CardBrand()

    /**
     * Discover card
     */
    object discover: CardBrand()

    /**
     * JCB card
     */
    object jcb: CardBrand()

    /**
     * Dinners Club card
     */
    object dinersClub: CardBrand()

    companion object {

        /**
         * @return array with all card brands
         */
        val allCases: Array<CardBrand>  get() = arrayOf(visa, mastercard,  americanExpress, discover, jcb, dinersClub)

        /**
         * Recognize the card brand of a credit card number
         *
         * @param cardNumber The card number string
         * @return the CardBrand otherwise null if no card brand match
         */
        fun fromCardNumber(cardNumber: String): CardBrand? {
            return allCases.filter { it.pattern.toRegex().matches(cardNumber.decimalDigits) }.firstOrNull()
        }

    }

    /**
     * Returns the correct card number length for validating card brand
     */
    override val panLength: IntArray get() {
        return when (this) {
            is visa -> intArrayOf(13, 16)
            is mastercard -> intArrayOf(16)
            is americanExpress -> intArrayOf(15)
            is discover -> intArrayOf(16)
            is jcb -> intArrayOf(16)
            is dinersClub -> intArrayOf(14)
        }
    }

    /**
     *  Returns the correct security code length for validating card brand
     */
    override val cvcLength: IntArray get() {
        return when (this) {
            is americanExpress -> intArrayOf(3, 4)
            else -> intArrayOf(3)
        }
    }

    /**
     * Returns the pattern to recognise a card brand
     */
    override val pattern: String get() {
        return when (this) {
            is visa -> "^4[0-9][0-9]{0,}$"
            is mastercard ->  "^(?:5[1-5]|2[2-7])[0-9]{0,}"
            is americanExpress -> "^3[47][0-9]{0,}"
            is discover -> "^(?:6[045]|622)[0-9]{0,}$"
            is jcb -> "^35[0-9]{0,}$"
            is dinersClub -> "^3[0689][0-9]{0,}$"
        }
    }

    /**
     * Returns regular expression string for formatting the card brand
     */
    override val format: Array<Int> get() {
        return when (this) {
            is americanExpress -> arrayOf(4, 6, 5)
            is dinersClub -> arrayOf(4, 6, 4)
            else -> arrayOf(4, 4, 4, 4)
        }
    }

    /**
     * Card brand printable
     */
    override val description: String get() {
        return when (this) {
            is visa -> "Visa"
            is mastercard ->  "Mastercard"
            is americanExpress -> "American Express"
            is discover -> "Discover"
            is jcb -> "JCB"
            is dinersClub -> "Diners Club"
        }
    }
}
