package io.paymenthighway.sdk.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import io.paymenthighway.sdk.R
import io.paymenthighway.sdk.model.CardData
import io.paymenthighway.sdk.model.Purchase

/**
 * Pay with Card Widget
 *
 * You can include this widget in any or your view's layout.
 * This permit the user to input credit card number, expiry date and security code
 * and add purchase information like Amount, currency, description
 *
 * ```
 *  import io.paymenthighway.sdk.ui.PayWithCardWidget
 *
 *
 *  internal lateinit var mPayWithCardWidget: PayWithCardWidget
 *
 *  mPayWithCardWidget = findViewById<PayWithCardWidget>(R.id.pay_with_card_widget)
 *
 *  @see PayWithCardActivity in the demo example
 * ```
 */
class PayWithCardWidget  @JvmOverloads constructor(context: Context, attr: AttributeSet? = null): LinearLayout(context, attr), ValidationListener {

    /**
     * Add card widget
     */
    val addCardWidget: AddCardWidget


    /**
     * Can be set to listen when the the card info are valid
     */
    var validationListener: ValidationListener? = null

    /**
     * Returns true is all the card info are valid
     */
    var isValid: Boolean = false
        get() {
            return addCardWidget.isValid
        }

    /**
     * return the CardData if available and valid otherwise null
     */
    var card: CardData? = null
        get() {
            return addCardWidget.card
        }

    /**
     * Purchase information
     */
    var purchase: Purchase? = null
        set(value: Purchase?) {
            sumLabel.text = value?.amountWithCurrency
            descriptionLabel.text = value?.description
        }

    private val sumLabel: TextView
    private val descriptionLabel: TextView

    init {
        orientation = LinearLayout.VERTICAL
        View.inflate(context, R.layout.pay_with_card_widget, this)

        addCardWidget = findViewById(R.id.add_card_widget)
        addCardWidget.validationListener = this
        sumLabel = findViewById(R.id.purchase_sum)
        descriptionLabel = findViewById(R.id.purchase_description)
    }

    override fun isValidDidChange(isValid: Boolean) {
        validationListener?.isValidDidChange(this.isValid)
    }
}
