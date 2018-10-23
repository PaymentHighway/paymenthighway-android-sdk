package io.paymenthighway.demo

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.widget.Button
import android.widget.ProgressBar
import io.paymenthighway.sdk.model.Purchase
import io.paymenthighway.sdk.ui.PayWithCardWidget
import io.paymenthighway.sdk.ui.setValidationListener

class PayWithCardActivity : BaseActivity()  {

    internal lateinit var mPayWithCardWidget: PayWithCardWidget

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay_with_card)
        mPayWithCardWidget = findViewById<PayWithCardWidget>(R.id.pay_with_card_widget)

        mPayWithCardWidget.setValidationListener {
            isValidDidChange {
                super.mOkButton.isEnabled = it
                if (it) {
                    hideKeyboard()
                }
            }
        }

        super.mOkButton = findViewById<Button>(R.id.pay_with_card_button)
        super.mCancelButton = findViewById<Button>(R.id.cancel_button)
        super.mProgressBar = findViewById<ProgressBar>(R.id.progress_bar)

        val purchase = intent.getParcelableExtra<Purchase>("PURCHASE")

        if (purchase != null) {
            mPayWithCardWidget.purchase = purchase
        }

        super.mOkAction = okAction@ {
            Snackbar.make(it, "TODO: Pay with card when api available!!!", Snackbar.LENGTH_LONG).show()
        }
    }
}
