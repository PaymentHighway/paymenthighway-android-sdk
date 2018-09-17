package io.paymenthighway.sdk.util


/**
 * String extension to return only numerical digits from the string
 */
val String.decimalDigits: String
    get() {
        val regex = "[^0-9]".toRegex()
        return regex.replace(this, "")
    }

/**
 * String extension to insert string at index
 *
 * @param index where the element have to be inserted
 * @param element to be inserted
 * @return new string
 */
fun String.insertAt(index: Int, element: String): String {
    val indexOk = index.takeIf { index <= this.length } ?: this.length
    return this.substring(0,indexOk)+element+this.substring(indexOk)
}
