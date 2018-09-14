package io.paymenthighway.sdk.ui

import android.content.Context
import android.support.design.widget.TextInputLayout
import android.support.v4.graphics.drawable.DrawableCompat
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import io.paymenthighway.sdk.CardBrand
import io.paymenthighway.sdk.R
import io.paymenthighway.sdk.model.CardData
import io.paymenthighway.sdk.model.ExpiryDate
import io.paymenthighway.sdk.util.decimalDigits

class AddCardWidget: LinearLayout, ValidationListener {

    val mCardNumberEditText: CardNumberEditText
    val mExpiryDateEditText: ExpiryDateEditText
    val mSecurityCodeEditText: SecurityCodeEditText

    var addCardWidgetValidationListener: ValidationListener? = null

    var isValid: Boolean = false
        get() {
            return mCardNumberEditText.isValid &&
                    mExpiryDateEditText.isValid &&
                    mSecurityCodeEditText.isValid
        }

    var card: CardData? = null
        get() {
            val pan = mCardNumberEditText.text?.toString() ?: return null
            val cvc: String = mSecurityCodeEditText.text?.toString() ?: return null
            val expiryDateString = mExpiryDateEditText.text?.toString() ?: return null
            val expiryDate = ExpiryDate.fromString(expiryDateString) ?: return null
            return CardData(pan.decimalDigits, cvc.decimalDigits, expiryDate)
        }

    constructor(context: Context?) : super(context) {}

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    init {
        orientation = LinearLayout.VERTICAL
        View.inflate(context, R.layout.add_card_widget, this)

        mCardNumberEditText = findViewById(R.id.edit_text_card_number)
        mExpiryDateEditText = findViewById(R.id.edit_text_expiry_date)
        mSecurityCodeEditText = findViewById(R.id.edit_text_security_code)

        mCardNumberEditText.editTextValidationListener = this
        mCardNumberEditText.textLayout = findViewById(R.id.text_layout_card_number)

        mExpiryDateEditText.editTextValidationListener = this
        mExpiryDateEditText.textLayout = findViewById(R.id.text_layout_expiry_date)

        mSecurityCodeEditText.editTextValidationListener = this
        mSecurityCodeEditText.textLayout = findViewById(R.id.text_layout_security_code)
        mSecurityCodeEditText.cardBrand = { CardBrand.fromCardNumber(mCardNumberEditText.text.toString()) }
    }

    override fun isValidDidChange(isValid: Boolean) {
        addCardWidgetValidationListener?.isValidDidChange(this.isValid)
        if (isValid) setFocusNext()
    }

    internal fun setFocusNext() {
        val editTexts = arrayOf(mCardNumberEditText, mExpiryDateEditText, mSecurityCodeEditText)
        val notValidTexts = editTexts.filter { !it.isValid }
        if (notValidTexts.size > 0) notValidTexts[0].requestFocus()
    }
}