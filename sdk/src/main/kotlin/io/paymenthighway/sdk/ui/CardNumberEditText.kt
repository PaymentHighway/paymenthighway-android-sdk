package io.paymenthighway.sdk.ui

import android.content.Context
import android.graphics.Rect
import android.text.InputFilter
import android.util.AttributeSet
import io.paymenthighway.sdk.CardBrand
import io.paymenthighway.sdk.R
import io.paymenthighway.sdk.model.CardData

class CardNumberEditText : EditText {

    constructor(context: Context?) : super(context) {}

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    init {
        hintText = resources.getString(R.string.card_number_hint)
        errorText = resources.getString(R.string.error_card_number)
        format = { CardData.formatCardNumber(it) }
        validate = { CardData.isCardNumberValid(it) }
    }

}