package io.paymenthighway.sdk.ui

/**
 * Interface to listen if isValid ui item did change
 */
interface ValidationListener {

    /**
     * Called every time the related validation state isValid did change
     *
     * @param isValid true if ui item is valid
     */
    fun isValidDidChange(isValid: Boolean)
}