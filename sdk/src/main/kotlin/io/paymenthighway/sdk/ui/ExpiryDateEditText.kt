package io.paymenthighway.sdk.ui

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.View
import io.paymenthighway.sdk.CardBrand
import io.paymenthighway.sdk.R
import io.paymenthighway.sdk.model.CardData
import io.paymenthighway.sdk.model.ExpirationDate

class ExpiryDateEditText : EditText {

    private var lastKeyDel = false

    constructor(context: Context?) : super(context) {}

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    init {
        hintText = resources.getString(R.string.expiry_date_hint)
        format = { ExpirationDate.format(it, lastKeyDel) }
        validate = { ExpirationDate.isValid(it) }
        setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            lastKeyDel = if (keyCode == KeyEvent.KEYCODE_DEL) true else false;
            false
        })
    }
}