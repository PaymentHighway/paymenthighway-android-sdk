package io.paymenthighway.sdk

import io.paymenthighway.sdk.model.CardData
import io.paymenthighway.sdk.util.decimalDigits
import org.junit.Assert
import org.junit.Test

val validCardBrands = arrayOf(
    Pair(CardBrand.americanExpress, arrayOf("378282246310005", "371449635398431", "378734493671000")),
    Pair(CardBrand.dinersClub, arrayOf("30569309025904", "38520000023237")),
    Pair(CardBrand.discover, arrayOf("6011111111111117", "6011000990139424")),
    Pair(CardBrand.jcb, arrayOf("3530111333300000", "3566002020360505")),
    Pair(CardBrand.mastercard, arrayOf("5555555555554444", "5105105105105100")),
    Pair(CardBrand.visa, arrayOf("4444333322221111", "4012888888881881", "4222222222222"))
)

val invalidCardBrands = arrayOf("378282246310006", "371449635398432", "305693090259043", "371449635398432", "4242424242424240", "9944333322221111")

val securityCodeFormats = arrayOf(
    Pair("", ""),
    Pair("0", "0"),
    Pair("00", "00"),
    Pair("000", "000"),
    Pair("0000", "0000"),
    Pair("00000", "0000"),
    Pair("41234", "4123"),
    Pair("123412344234", ""),
    Pair(" 1 43 / 2k k nkm", "1432")
)

internal class CardDataTest {

    @Test
    fun testRemoveIllegalCharacters() {
        val actualCardNumber = "55555🐺5555a5554 44,. -4".decimalDigits
        Assert.assertEquals("5555555555554444",actualCardNumber)
    }

    @Test
    fun testNoRemoveOfCharacters() {
        val actualCardNumber = "378282246310005".decimalDigits
        Assert.assertEquals("378282246310005", actualCardNumber)
    }

    @Test
    fun testGetCardBrandFromCardNumber() {
        for (item in validCardBrands) {
            for (cardNumber in item.second) {
                // Should recognise brand with just 2 card digits
                val foundCardBrand = CardBrand.fromCardNumber(cardNumber.take(2))
                if (foundCardBrand != null) {
                    Assert.assertEquals(item.first, foundCardBrand)
                } else {
                    Assert.fail("Card brand ${item.first} not found")
                }
            }
        }
    }

    @Test
    fun testFormatCardNumber() {
        // VISA
        Assert.assertEquals("5555 5555 5555 4444", CardData.formatCardNumber("5555555555554444"))
        // AMEX
        Assert.assertEquals("3782 822463 10005", CardData.formatCardNumber("378282246310005"))
        // DINNER
        Assert.assertEquals("3056 930902 5904", CardData.formatCardNumber("30569309025904"))
    }

    @Test
    fun testIsCardNumberValid() {
        for ((brand, cardNumbers) in validCardBrands) {
            for (cardNumber in cardNumbers) {
                Assert.assertTrue(CardData.isCardNumberValid(cardNumber))
            }
        }
    }

    @Test
    fun testIsCardNumberInvalid() {
        for (cardNumber in invalidCardBrands) {
            Assert.assertFalse(CardData.isCardNumberValid(cardNumber))
        }
    }

    @Test
    fun testFormatSecurityCOde() {
        for ((securityCode, expectedSecurityCode) in securityCodeFormats) {
            Assert.assertEquals(expectedSecurityCode, CardData.formatSecurityCode(securityCode))
        }
    }
}