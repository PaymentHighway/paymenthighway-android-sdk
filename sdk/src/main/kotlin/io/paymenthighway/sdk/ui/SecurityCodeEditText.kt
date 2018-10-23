package io.paymenthighway.sdk.ui

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import io.paymenthighway.sdk.CardBrand
import io.paymenthighway.sdk.R
import io.paymenthighway.sdk.model.CardData

internal const val DefaultCVCLength = 3

/**
 * Specialized text field for collecting security code.
 */
class SecurityCodeEditText @JvmOverloads constructor(context: Context, attr: AttributeSet? = null): EditText(context, attr) {

    /**
     * Security code validation, format and hint depend of the card brand
     */
    var cardBrand: () -> CardBrand? = { null }

    /**
     * Initialize the EditText with specific formatter, validator and error texts
     *
     * @see CardData.formatSecurityCode
     * @see CardData.isSecurityCodeValid
     */
    init {
        errorText = resources.getString(R.string.error_security_code)
        format = { CardData.formatSecurityCode(it, cardBrand()) }
        validate = { CardData.isSecurityCodeValid(it, cardBrand()) }
    }

    /**
     * The hint text can change depending of the card brand
     */
    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        val cvvLenght = cardBrand()?.cvcLength?.max() ?: DefaultCVCLength
        hintText = resources.getString(if (cvvLenght == DefaultCVCLength) R.string.security_code_hint_3 else R.string.security_code_hint_4)
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
    }
}