package io.paymenthighway.sdk.model

import io.paymenthighway.sdk.util.decimalDigits
import io.paymenthighway.sdk.util.insertAt
import java.util.Calendar

data class ExpirationDate(val month: String, val year: String) {

    companion object {
        fun fromString(expirationDate: String) : ExpirationDate?  {
            val monthYear = expirationDateComponents(expirationDate) ?: return null
            val monthString = "%02d".format(monthYear.first)
            val yearString = "%04d".format( if (monthYear.second <= 999) monthYear.second +2000  else monthYear.second)
            return ExpirationDate(monthString, yearString)
        }

        fun isValid(expirationDateString: String): Boolean {
            val monthYear = expirationDateComponents(expirationDateString) ?: return false
            // Calendar month from 0-11
            val currentMonth = Calendar.getInstance().get(Calendar.MONTH)+1
            val currentYear = Calendar.getInstance().get(Calendar.YEAR)
            return ((currentYear < monthYear.second) or ((currentYear == monthYear.second) and (currentMonth < monthYear.first)))
        }

        private fun expirationDateComponents(expirationDate: String, separator: String = "/"): Pair<Int, Int>? {
            val components = expirationDate.split(separator)
            if (components.size != 2) return null
            val monthNumber: Int = try { components[0].toInt() } catch (e: Exception) { return null }
            val yearNumber: Int = try { components[1].toInt() } catch (e: Exception) { return null }
            if ((monthNumber !in 1..12) or (yearNumber < 0)) return null
            return Pair(monthNumber, if (yearNumber <= 999) yearNumber + 2000  else yearNumber)
        }

        fun format(expirationDate: String, deleting: Boolean = false): String {
            var onlyDigitsExpirationDate = expirationDate.decimalDigits
            return when (onlyDigitsExpirationDate.length) {
                1 -> if (onlyDigitsExpirationDate.toInt() in 2..9) "0$onlyDigitsExpirationDate/" else onlyDigitsExpirationDate
                2 -> {
                    val digitsAsInt = onlyDigitsExpirationDate.toInt()
                    if ((deleting && expirationDate.length == 2) || ((digitsAsInt < 1 || digitsAsInt > 12))) {
                        onlyDigitsExpirationDate = onlyDigitsExpirationDate.removeRange(1, 2)
                        onlyDigitsExpirationDate
                    } else {
                        onlyDigitsExpirationDate =  onlyDigitsExpirationDate+"/"
                        onlyDigitsExpirationDate
                    }
                }
                3, 4, 5 -> {
                    if (onlyDigitsExpirationDate.length == 5) onlyDigitsExpirationDate = onlyDigitsExpirationDate.removeRange(4, 5)
                    onlyDigitsExpirationDate = onlyDigitsExpirationDate.insertAt(2,"/")
                    onlyDigitsExpirationDate
                }
                else -> ""
            }
        }
    }
}