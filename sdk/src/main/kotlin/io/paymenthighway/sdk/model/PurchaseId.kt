package io.paymenthighway.sdk.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Data class to hold purchase id
 *
 * @param id Actual purchase id as String
 */
data class PurchaseId(val id: String): Parcelable {

    constructor(parcel: Parcel) : this(parcel.readString())

    /**
     * Return the actual string value representation the object
     */
    override fun toString(): String {
        return id
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PurchaseId> {
        override fun createFromParcel(parcel: Parcel): PurchaseId {
            return PurchaseId(parcel)
        }

        override fun newArray(size: Int): Array<PurchaseId?> {
            return arrayOfNulls(size)
        }
    }
}