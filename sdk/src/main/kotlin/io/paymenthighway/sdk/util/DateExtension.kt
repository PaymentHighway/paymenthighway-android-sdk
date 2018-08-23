package io.paymenthighway.sdk.util

import java.text.SimpleDateFormat
import java.util.*

fun Date.timestamp(): String {

    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
    dateFormat.timeZone = TimeZone.getTimeZone("UTC")
    return dateFormat.format(this)
}
