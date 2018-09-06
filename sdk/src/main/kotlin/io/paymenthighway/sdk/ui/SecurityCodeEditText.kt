package io.paymenthighway.sdk.ui

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import io.paymenthighway.sdk.CardBrand
import io.paymenthighway.sdk.R
import io.paymenthighway.sdk.model.CardData

class SecurityCodeEditText : EditText {

    var cardBrand: () -> CardBrand? = { null }

    constructor(context: Context?) : super(context) {}

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    init {
        format = { CardData.formatSecurityCode(it) }
        validate = { CardData.isSecurityCodeValid(it, cardBrand()) }
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        val cvvLenght = cardBrand()?.cvcLength?.max() ?: 3
        hintText = resources.getString(if (cvvLenght == 3) R.string.security_code_hint_3 else R.string.security_code_hint_4)
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
    }
}