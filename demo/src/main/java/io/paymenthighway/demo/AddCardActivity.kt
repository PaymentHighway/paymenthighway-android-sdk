package io.paymenthighway.demo

import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ProgressBar
import io.paymenthighway.demo.backendadapterexample.BackendAdapterExample
import io.paymenthighway.demo.backendadapterexample.TransactionToken
import io.paymenthighway.sdk.Environment
import io.paymenthighway.sdk.PaymentContext
import io.paymenthighway.sdk.model.AccountId
import io.paymenthighway.sdk.model.MerchantId
import io.paymenthighway.sdk.model.PaymentConfig
import io.paymenthighway.sdk.ui.*
import io.paymenthighway.sdk.util.Result

class AddCardActivity : BaseActivity() {

    internal lateinit var mCardInputWidget: AddCardWidget

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_card)

        mCardInputWidget = findViewById<AddCardWidget>(R.id.add_card_widget)

        // Example For ValidationListener as delegate see below implementation
        // mCardInputWidget.validationListener = this

        mCardInputWidget.setValidationListener {
            isValidDidChange {
                super.mOkButton.isEnabled = it
                if (it) {
                    hideKeyboard()
                }
            }
        }

        super.mOkButton = findViewById<Button>(R.id.add_card_button)
        super.mCancelButton = findViewById<Button>(R.id.cancel_button)
        super.mProgressBar = findViewById<ProgressBar>(R.id.progress_bar)

        super.mOkAction = okAction@ {
            val cardData = mCardInputWidget.card
            if (cardData == null) {
                Snackbar.make(it, "Invalid card data!", Snackbar.LENGTH_LONG).show()
                return@okAction
            }
            super.progressBarVisible(true)
            super.mPaymentContext.addCard(cardData) {result ->
                runOnUiThread {
                    super.progressBarVisible(false)
                    when (result) {
                        is Result.Success -> {
                            val transactionToken = result.value
                            val message = """
                                Add Card Completed

                                token: ${transactionToken.token.substring(0,8)}...
                                brand: ${transactionToken.card.cardType}
                                last digits:  ${transactionToken.card.partialPan}
                                expiry date:  ${transactionToken.card.expireMonth}/${transactionToken.card.expireYear}
                            """.trimIndent()
                            super.showAlertDialog("Add card completed", message) {
                                finish()
                            }
                        }
                        is Result.Failure -> {
                            Snackbar.make(it, result.error.message ?: "Error in adding card!", Snackbar.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }
}