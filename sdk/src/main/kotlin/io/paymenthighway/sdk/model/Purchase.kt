package io.paymenthighway.sdk.model

import android.os.Parcel
import android.os.Parcelable
import java.text.NumberFormat
import java.util.*

data class Purchase(val purchaseId: PurchaseId, val currency: String, val amount: Double, val description: String): Parcelable {
    var amountWithCurrency: String = ""
        get() {
            val format = NumberFormat.getCurrencyInstance(Locale.getDefault())
            format.currency = java.util.Currency.getInstance(currency)
            return format.format(amount)
        }

    constructor(parcel: Parcel) : this(
            parcel.readParcelable(PurchaseId::class.java.classLoader),
            parcel.readString(),
            parcel.readDouble(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(purchaseId,0)
        parcel.writeString(currency)
        parcel.writeDouble(amount)
        parcel.writeString(description)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Purchase> {
        override fun createFromParcel(parcel: Parcel): Purchase {
            return Purchase(parcel)
        }

        override fun newArray(size: Int): Array<Purchase?> {
            return arrayOfNulls(size)
        }
    }
}