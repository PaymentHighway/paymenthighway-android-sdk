package io.paymenthighway.demo

import android.content.Context
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
import io.paymenthighway.sdk.ui.ValidationListener

open class BaseActivity : AppCompatActivity(), ValidationListener {

    protected lateinit var mOkButton: Button
    protected lateinit var mCancelButton: Button
    protected lateinit var mProgressBar: ProgressBar

    protected lateinit  var mPaymentContext: PaymentContext<TransactionToken>

    protected var mOkAction: (View) -> Unit = {}

    override fun onStart() {
        super.onStart()

        mCancelButton.setOnClickListener(View.OnClickListener { view ->
            finish()
        })

        mOkButton.isEnabled = false
        mOkButton.setOnClickListener(View.OnClickListener { view ->
            mOkAction(view)
        })

        val backendAdapter = BackendAdapterExample()
        val paymentConfig = PaymentConfig(merchantId, accountId, Environment.Sandbox)
        mPaymentContext = PaymentContext(paymentConfig, backendAdapter)
    }

    override fun isValidDidChange(isValid: Boolean) {
        mOkButton.isEnabled = isValid
        if (isValid) {
            hideKeyboard()
        }
    }

    protected fun hideKeyboard() {
        if (currentFocus == null) return
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
    }

    protected fun showAlertDialog(title: String, message: String, completion: () -> Unit) {
        val builder = AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK") {  _, _ ->
                    completion()
                }
        val dialog = builder.create()
        dialog.show()
    }

    protected fun progressBarVisible(visible: Boolean) {
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