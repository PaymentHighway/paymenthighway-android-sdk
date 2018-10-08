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

class ValidationListenerHelper: ValidationListener {

    private var isValidDidChangeListener: IsValidDidChangeListener? = null

    fun isValidDidChange(isValidDidChange: IsValidDidChangeListener) {
        this.isValidDidChangeListener = isValidDidChange
    }

    override fun isValidDidChange(isValid: Boolean) {
        this.isValidDidChangeListener?.invoke(isValid)
    }
}

private typealias IsValidDidChangeListener = (Boolean) -> Unit

fun AddCardWidget.setValidationListener(init: ValidationListenerHelper.() -> Unit) {
    val listener = ValidationListenerHelper()
    listener.init()
    this.addCardWidgetValidationListener = listener
}