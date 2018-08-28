package io.paymenthighway.sdk

import io.paymenthighway.sdk.util.decimalDigits

interface CardBrandData {
    val panLength: IntArray
    val cvcLength: IntArray
    val pattern: String
    val format: Array<Int>
    val description: String

    fun isValid(cardNumber: String): Boolean
}

sealed class CardBrand: CardBrandData {

    object visa: CardBrand()
    object mastercard: CardBrand()
    object americanExpress: CardBrand()
    object discover: CardBrand()
    object jcb: CardBrand()
    object dinersClub: CardBrand()

    companion object {

        val allCases: Array<CardBrand>  get() = arrayOf(visa, mastercard,  americanExpress, discover, jcb, dinersClub)

        fun fromCardNumber(cardNumber: String): CardBrand? {
            return allCases.filter { it.pattern.toRegex().matches(cardNumber.decimalDigits) }.firstOrNull()
        }

    }

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

    override val cvcLength: IntArray get() {
        return when (this) {
            is americanExpress -> intArrayOf(3, 4)
            else -> intArrayOf(3)
        }
    }

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

    override val format: Array<Int> get() {
        return when (this) {
            is americanExpress -> arrayOf(4, 6, 5)
            is dinersClub -> arrayOf(4, 6, 4)
            else -> arrayOf(4, 4, 4, 4)
        }
    }

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

    override fun isValid(cardNumber: String): Boolean = this.panLength.contains(cardNumber.count())
}
