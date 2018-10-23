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
class AddCardWidget @JvmOverloads constructor(context: Context, attr: AttributeSet? = null): LinearLayout(context, attr), ValidationListener {

    /**
     * Card number edit text
     */
    val cardNumberEditText: CardNumberEditText

    /**
     * Expiry date edit text
     */
    val expiryDateEditText: ExpiryDateEditText

    /**
     * Security code edit text
     */
    val securityCodeEditText: SecurityCodeEditText

    /**
     * Can be set to listen when the the card info are valid
     */
    var validationListener: ValidationListener? = null

    /**
     * Returns true is all the card info are valid
     */
    var isValid: Boolean = false
        get() {
            return cardNumberEditText.isValid &&
                    expiryDateEditText.isValid &&
                    securityCodeEditText.isValid
        }

    /**
     * return the CardData if available and valid otherwise null
     */
    var card: CardData? = null
        get() {
            val pan = cardNumberEditText.text?.toString() ?: return null
            val cvc: String = securityCodeEditText.text?.toString() ?: return null
            val expiryDateString = expiryDateEditText.text?.toString() ?: return null
            val expiryDate = ExpiryDate.fromString(expiryDateString) ?: return null
            return CardData(pan.decimalDigits, cvc.decimalDigits, expiryDate)
        }

    init {
        orientation = LinearLayout.VERTICAL
        View.inflate(context, R.layout.add_card_widget, this)

        cardNumberEditText = findViewById(R.id.edit_text_card_number)
        expiryDateEditText = findViewById(R.id.edit_text_expiry_date)
        securityCodeEditText = findViewById(R.id.edit_text_security_code)

        cardNumberEditText.editTextValidationListener = this
        cardNumberEditText.textLayout = findViewById(R.id.text_layout_card_number)

        expiryDateEditText.editTextValidationListener = this
        expiryDateEditText.textLayout = findViewById(R.id.text_layout_expiry_date)

        securityCodeEditText.editTextValidationListener = this
        securityCodeEditText.textLayout = findViewById(R.id.text_layout_security_code)
        securityCodeEditText.cardBrand = { CardBrand.fromCardNumber(cardNumberEditText.text.toString()) }
    }

    override fun isValidDidChange(isValid: Boolean) {
        validationListener?.isValidDidChange(this.isValid)
        if (isValid) setFocusNext()
    }

    internal fun setFocusNext() {
        val editTexts = arrayOf(cardNumberEditText, expiryDateEditText, securityCodeEditText)
        val notValidTexts = editTexts.filter { !it.isValid }
        if (notValidTexts.size > 0) notValidTexts[0].requestFocus()
    }
}
