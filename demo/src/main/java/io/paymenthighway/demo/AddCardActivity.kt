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
import io.paymenthighway.sdk.PaymentContext
import io.paymenthighway.sdk.model.AccountId
import io.paymenthighway.sdk.model.MerchantId
import io.paymenthighway.sdk.model.PaymentConfig
import io.paymenthighway.sdk.ui.AddCardWidget
import io.paymenthighway.sdk.ui.ValidationListener
import io.paymenthighway.sdk.util.Result

class AddCardActivity : AppCompatActivity(), ValidationListener {

    internal lateinit var mCardInputWidget: AddCardWidget
    internal lateinit var mAddCardButton: Button
    internal lateinit var mProgressBar: ProgressBar

    internal  lateinit  var mPaymentContext: PaymentContext<TransactionToken>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_card)

        mCardInputWidget = findViewById<AddCardWidget>(R.id.add_card_widget)
        mCardInputWidget.addCardWidgetValidationListener = this

        mAddCardButton = findViewById<Button>(R.id.add_card_button)
        mProgressBar = findViewById<ProgressBar>(R.id.progress_bar)

        mAddCardButton.isEnabled = false
        mAddCardButton.setOnClickListener(View.OnClickListener { view ->
            val cardData = mCardInputWidget.card
            if (cardData == null) {
                Snackbar.make(view, "Invalid card data!", Snackbar.LENGTH_LONG).show()
                return@OnClickListener
            }
            progressBarVisible(true)
            mPaymentContext.addCard(cardData) {result ->
                runOnUiThread {
                    progressBarVisible(false)
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
                            showAlertDialog("Add card completed", message) {
                                finish()
                            }
                        }
                        is Result.Failure -> {
                            Snackbar.make(view, result.error.message ?: "Error in adding card!", Snackbar.LENGTH_LONG).show()
                        }
                    }
                }
            }
        })

        val backendAdapter = BackendAdapterExample()
        val paymentConfig = PaymentConfig(merchantId, accountId)
        mPaymentContext = PaymentContext(paymentConfig, backendAdapter)
    }

    override fun isValidDidChange(isValid: Boolean) {
        mAddCardButton.isEnabled = isValid
        if (isValid) {
            val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus.windowToken, 0)
        }
    }

    private fun showAlertDialog(title: String, message: String, completion: () -> Unit) {
        val builder = AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK") {  _, _ ->
                    completion()
                }
        val dialog = builder.create()
        dialog.show()
    }

    private fun progressBarVisible(visible: Boolean) {
        if (visible) {
            mProgressBar.visibility = View.VISIBLE
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                 WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        } else {
            mProgressBar.visibility = View.INVISIBLE
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
    }

    companion object {

        internal val merchantId = MerchantId("test_merchantId")
        internal val accountId = AccountId("test")
    }

}