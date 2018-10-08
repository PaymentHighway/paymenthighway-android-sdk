package io.paymenthighway.sdk.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import io.paymenthighway.sdk.CardBrand
import io.paymenthighway.sdk.R
import io.paymenthighway.sdk.model.CardData
import io.paymenthighway.sdk.model.ExpiryDate
import io.paymenthighway.sdk.util.decimalDigits

/**
 * Add Card Widget
 *
 * You can include this widget in any or your view's layout.
 * This permit the user to input credit card number, expiry date and security code
 *
 * ```
 *  import io.paymenthighway.sdk.ui.AddCardWidget
 *
 *
 *  internal lateinit var mCardInputWidget: AddCardWidget
 *
 *  mCardInputWidget = findViewById<AddCardWidget>(R.id.add_card_widget)
 *
 *  @see AddCardActivity in the demo example
 * ```
 */
class AddCardWidget: LinearLayout, ValidationListener {

    /**
     * Card number edit text
     */
    val mCardNumberEditText: CardNumberEditText

    /**
     * Expiry date edit text
     */
    val mExpiryDateEditText: ExpiryDateEditText

    /**
     * Security code edit text
     */
    val mSecurityCodeEditText: SecurityCodeEditText

    /**
     * Can be set to listen when the the card info are valid
     */
    var addCardWidgetValidationListener: ValidationListener? = null

    /**
     * Returns true is all the card info are valid
     */
    var isValid: Boolean = false
        get() {
            return mCardNumberEditText.isValid &&
                    mExpiryDateEditText.isValid &&
                    mSecurityCodeEditText.isValid
        }

    /**
     * return the CardData if available and valid otherwise null
     */
    var card: CardData? = null
        get() {
            val pan = mCardNumberEditText.text?.toString() ?: return null
            val cvc: String = mSecurityCodeEditText.text?.toString() ?: return null
            val expiryDateString = mExpiryDateEditText.text?.toString() ?: return null
            val expiryDate = ExpiryDate.fromString(expiryDateString) ?: return null
            return CardData(pan.decimalDigits, cvc.decimalDigits, expiryDate)
        }

    /**
     * LinearLayout constructor
     */
    constructor(context: Context?) : super(context) {}

    /**
     * LinearLayout constructor
     */
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}

    /**
     * LinearLayout constructor
     */
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
