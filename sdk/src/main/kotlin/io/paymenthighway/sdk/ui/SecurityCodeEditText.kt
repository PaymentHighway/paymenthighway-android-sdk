package io.paymenthighway.sdk.ui

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import io.paymenthighway.sdk.CardBrand
import io.paymenthighway.sdk.R
import io.paymenthighway.sdk.model.CardData

const val DefaultCVCLength = 3

class SecurityCodeEditText : EditText {

    var cardBrand: () -> CardBrand? = { null }

    constructor(context: Context?) : super(context) {}

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    init {
        errorText = resources.getString(R.string.error_security_code)
        format = { CardData.formatSecurityCode(it, cardBrand()) }
        validate = { CardData.isSecurityCodeValid(it, cardBrand()) }
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        val cvvLenght = cardBrand()?.cvcLength?.max() ?: DefaultCVCLength
        hintText = resources.getString(if (cvvLenght == DefaultCVCLength) R.string.security_code_hint_3 else R.string.security_code_hint_4)
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
    }
}