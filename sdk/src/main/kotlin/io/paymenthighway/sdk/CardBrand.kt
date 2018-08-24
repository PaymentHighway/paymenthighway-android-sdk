package io.paymenthighway.sdk

interface CardBrandData {
    val panLength: IntArray
    val cvcLength: IntArray
    val matcher: String
    val description: String
}

sealed class CardBrand: CardBrandData {

    object visa: CardBrand()
    object mastercard: CardBrand()
    object americanExpress: CardBrand()
    object discover: CardBrand()
    object jcb: CardBrand()
    object dinersClub: CardBrand()

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

    override val matcher: String get() {
        return when (this) {
            is visa -> "^4[0-9]{6,}$"
            is mastercard ->  "^5[1-5][0-9]{5,}$"
            is americanExpress -> "^3[47][0-9]{5,}$"
            is discover -> "^6(?:011|5[0-9]{2})[0-9]{3,}$"
            is jcb -> "^(?:2131|1800|35[0-9]{3})[0-9]{3,}$"
            is dinersClub -> "^3(?:0[0-5]|[68][0-9])[0-9]{4,}$"
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

}
