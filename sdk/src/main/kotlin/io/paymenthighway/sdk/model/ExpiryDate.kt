package io.paymenthighway.sdk.model

import io.paymenthighway.sdk.util.decimalDigits
import io.paymenthighway.sdk.util.insertAt
import java.util.Calendar

/**
 * Data class to hold expiry data
 *
 * @param id Actual account id as String
 * @param month 1-2 numeric chars, accepted 1-12
 * @param year 1-4 numeric chars. For years <= 999 2000 is added
 *
 * You can check if expiry date is valid with isValid
 *
 */
data class ExpiryDate(val month: String, val year: String) {

    /**
     * Check if the expiry date is valid
     */
    val isValid: Boolean
        get() {
            return ExpiryDate.isValid("$month/$year")
        }

    companion object {

        /**
         * Get ExpiryDate from raw expiration date string
         *
         * month will formatted as MM
         * year will formatted as YYYY
         *
         * @param expiryDate raw expiration date, format "M[M]/Y[YYY]"
         * @return ExpiraryDate or null if the expiry date is invalid
         */
        fun fromString(expiryDate: String) : ExpiryDate?  {
            val monthYear = expiryDateComponents(expiryDate) ?: return null
            val monthString = "%02d".format(monthYear.first)
            val yearString = "%04d".format( if (monthYear.second <= 999) monthYear.second +2000  else monthYear.second)
            return ExpiryDate(monthString, yearString)
        }

        /**
         * Check if expiration date is valid
         *
         * @param expiryDate raw expiration date, format "M[M]/Y[YYY]"
         * @return true if the date is valid
         */
        fun isValid(expiryDateString: String): Boolean {
            val monthYear = expiryDateComponents(expiryDateString) ?: return false
            // Calendar month from 0-11
            val currentMonth = Calendar.getInstance().get(Calendar.MONTH)+1
            val currentYear = Calendar.getInstance().get(Calendar.YEAR)
            return ((currentYear < monthYear.second) or ((currentYear == monthYear.second) and (currentMonth < monthYear.first)))
        }

        private fun expiryDateComponents(expiryDate: String, separator: String = "/"): Pair<Int, Int>? {
            val components = expiryDate.split(separator)
            if (components.size != 2) return null
            val monthNumber: Int = try { components[0].toInt() } catch (e: Exception) { return null }
            val yearNumber: Int = try { components[1].toInt() } catch (e: Exception) { return null }
            if ((monthNumber !in 1..12) or (yearNumber < 0)) return null
            return Pair(monthNumber, if (yearNumber <= 999) yearNumber + 2000  else yearNumber)
        }

        /**
         * Format the expiration date
         * ```
         *    example: previous input  user input  formatted
         *                                2           02/
         *                0               8           08/
         *                01/             2           01/2
         *                08/             <del>       0
         *                08/2            <del>       08/
         * ```
         *
         * @param expiryDate input expiration date
         * @param deleting need to know if there has been a delete since format change based on it(check example above)
         * @return expiry date formatted
         */
        fun format(expiryDate: String, deleting: Boolean = false): String {
            var onlyDigitsExpiryDate = expiryDate.decimalDigits
            return when (onlyDigitsExpiryDate.length) {
                1 -> if (onlyDigitsExpiryDate.toInt() in 2..9) "0$onlyDigitsExpiryDate/" else onlyDigitsExpiryDate
                2 -> {
                    val digitsAsInt = onlyDigitsExpiryDate.toInt()
                    if ((deleting && expiryDate.length == 2) || ((digitsAsInt < 1 || digitsAsInt > 12))) {
                        onlyDigitsExpiryDate.removeRange(1, 2)
                    } else {
                        onlyDigitsExpiryDate+"/"
                    }
                }
                in 3..5 -> {
                    if (onlyDigitsExpiryDate.length == 5) onlyDigitsExpiryDate = onlyDigitsExpiryDate.removeRange(4, 5)
                    onlyDigitsExpiryDate.insertAt(2,"/")
                }
                else -> ""
            }
        }
    }
}