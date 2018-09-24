package io.paymenthighway.sdk

import io.paymenthighway.sdk.model.ExpiryDate
import org.junit.Assert
import org.junit.Test

val expiryDateFormatTest = arrayOf(
    Triple("" , "", false),
    Triple("0" , "0", false),
    Triple("00" , "0", false),
    Triple("1" , "1", false),
    Triple("11" , "11/", false),
    Triple("12" , "1", true), // deleting case
    Triple("11/" , "11/", false),
    Triple("12/" , "12/", true), // deleting case
    Triple("13" , "1", false),
    Triple("23" , "2", false),
    Triple("10/1" , "10/1", false),
    Triple("10/" , "10/", false),
    Triple("8" , "08/", false),
    Triple("10/13" , "10/13", false),
    Triple("10/134" , "10/13", false),
    Triple("10/1334" , "", false),
    Triple("aas/df1" , "1", false)
)

val expiryDateValidTest = arrayOf(
    Pair("", false),
    Pair("11/", false),
    Pair("/23", false),
    Pair("10/13", false), // expiration date in the past
    Pair("13/29", false),
    Pair("12/29", true)
)

val expiryDateFromStringTest = arrayOf(
    Triple("1/1", "01", "2001"),
    Triple("1/-20", null, null),
    Triple("13/20", null, null),
    Triple("12/20", "12", "2020"),
    Triple("12/2012", "12", "2012")
)

internal class ExpiryDateTest {

    @Test
    fun testFormat() {
        for ((expiryDate, expectedExpiryDate, deleting) in expiryDateFormatTest) {
            val result = ExpiryDate.format(expiryDate, deleting)
            Assert.assertEquals(expectedExpiryDate, result)
        }
    }

    @Test
    fun testIsValid() {
        for ((expiryDate, expectedIsValid) in expiryDateValidTest) {
            val isValid = ExpiryDate.isValid(expiryDate)
            Assert.assertEquals(expectedIsValid, isValid)
        }
    }

    @Test
    fun testFromString() {
        for ((expiryDate, expectedMonth, expectedYear) in expiryDateFromStringTest) {
            val result = ExpiryDate.fromString(expiryDate)
            if (result == null) {
                Assert.assertEquals(expectedMonth, result)
            } else {
                Assert.assertEquals(expectedMonth, result.month)
                Assert.assertEquals(expectedYear, result.year)
            }
        }
    }
}