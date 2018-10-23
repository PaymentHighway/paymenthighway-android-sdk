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

private typealias IsValidDidChangeListener = (Boolean) -> Unit

/**
 * Helper to use lambdas instead of delegate ValidationListener
 */
class ValidationListenerHelper: ValidationListener {

    private var isValidDidChangeListener: IsValidDidChangeListener? = null

    fun isValidDidChange(isValidDidChange: IsValidDidChangeListener) {
        this.isValidDidChangeListener = isValidDidChange
    }

    override fun isValidDidChange(isValid: Boolean) {
        this.isValidDidChangeListener?.invoke(isValid)
    }
}

/**
 * Extension to AddCardWidget in order to use lambdas instead of delegate ValidationListener
 */
fun AddCardWidget.setValidationListener(init: ValidationListenerHelper.() -> Unit) {
    val listener = ValidationListenerHelper()
    listener.init()
    this.validationListener = listener
}

/**
 * Extension to PayWithCardWidget in order to use lambdas instead of delegate ValidationListener
 */
fun PayWithCardWidget.setValidationListener(init: ValidationListenerHelper.() -> Unit) {
    val listener = ValidationListenerHelper()
    listener.init()
    this.validationListener = listener
}