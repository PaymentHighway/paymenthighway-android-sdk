package io.paymenthighway.sdk.util


val String.decimalDigits: String
    get() {
        val regex = "[^0-9 ]".toRegex()
        return regex.replace(this, "")
    }

fun String.insertAt(index: Int, element: String): String {
    val indexOk = index.takeIf { index <= this.length } ?: this.length
    return this.substring(0,indexOk)+element+this.substring(indexOk)
}