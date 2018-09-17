
# Payment Highway Android Demo

This example app demonstrates how to integrate Android Payment Highway SDK for add card and payment.

## Mobile Application Backend

For the demo has been implemented a mobile application backend that use the [Payment Highway development sandbox](https://dev.paymenthighway.io/#development-sandbox).
For more info see the code [here]().

In client side you need to implement the BackendAdapter interface. 

The demo include a implementation example for a `BackendApapter` in the module `backendadapterexample`.

## Add a credit card

In order to add credit card and get a payment token you need a `PaymentContext`.

PaymentHighway Android SDK (debug) in connected with the [Payment Highway development sandbox](https://dev.paymenthighway.io/#development-sandbox).

Therefore the demo app use the provided sandbox merchant id and account id.

Example how istantiate a `PaymentContext`:
Kotlin:
```kotlin
    val merchantId = MerchantId("test_merchantId")
    val accountId = AccountId("test")

    lateinit  var mPaymentContext: PaymentContext<TransactionToken>

    val backendAdapter = BackendAdapterExample()
    val paymentConfig = PaymentConfig(merchantId, accountId)
    mPaymentContext = PaymentContext(paymentConfig, backendAdapter)
```

If you have the card data available (for example from your own form) then you can call `addCard`:
```kotlin
    val cardData = CardData(cardNumberFromForm, cvcFromForm, ExpiryDate(expirationDateFromForm))
    mPaymentContext.addCard(cardData) {result ->
        runOnUiThread {
            progressBarVisible(false)
            when (result) {
                is Result.Success -> {
                    val transactionToken = result.value
                    // do the payment with the transaction Token
                }
                is Result.Failure -> {
                    val error = result.error
                    // manage the error
                }
            }
        }
    }    
```

## AddCardViewController

Payment Highway SDK provide UI Form to input a credit card.

You need to add in your layout the AddCardWidget

```xml
    <io.paymenthighway.sdk.ui.AddCardWidget
        android:id="@+id/add_card_widget"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_margin="15dp"
        tools:layout_editor_absoluteX="15dp"
        tools:layout_editor_absoluteY="0dp" />
```

In your activity you can add the reference to it in this way:
```kotlin
    import io.paymenthighway.sdk.PaymentContext

    lateinit var mCardInputWidget: AddCardWidget
    
    mCardInputWidget = findViewById<AddCardWidget>(R.id.add_card_widget)
```

You can listen when the card data is valid implementing in your Activity (for example) the ValidationListener iterface:
```kotlin
    class AddCardActivity : AppCompatActivity(), ValidationListener 

        mCardInputWidget = findViewById<AddCardWidget>(R.id.add_card_widget)
        mCardInputWidget.addCardWidgetValidationListener = this

        override fun isValidDidChange(isValid: Boolean) {
            mAddCardButton.isEnabled = isValid
        }
    }  
```

Then you can add the card and get (if implemented in backendadapter) a payment token:
```kotlin
    val cardData = mCardInputWidget.card
     mPaymentContext.addCard(cardData) {result ->
        runOnUiThread {
            progressBarVisible(false)
            when (result) {
                is Result.Success -> {
                    val transactionToken = result.value
                    // do the payment with the transaction Token
                }
                is Result.Failure -> {
                    val error = result.error
                    // manage the error
                }
            }
        }
    }    

```

## How customize appearence of the Payment Highway UI Elements

You can define your own appearence attributes for the Payment Highway UI Elements.

SDK provide Styles for the TextInputLayout, for the EditText and for the ErrorTextAppearance.

```xml
    <style name="PHTextInputLayoutStyle" parent="Widget.Design.TextInputLayout">
        <item name="android:textColorHint">@color/floatingActiveLabelColor</item>
        <item name="colorControlNormal">@color/floatingLabelColor</item>
        <item name="colorControlActivated">@color/floatingActiveLabelColor</item>
    </style>

    <style name="PHEditTextStyle" parent="@android:style/Widget.EditText">
        <item name="android:textColor">@drawable/edit_text_color</item>
        <item name="android:textColorHint">@color/hintColor</item>
    </style>

    <style name="PHErrorAppearanceStyle" parent="@android:style/TextAppearance">
        <item name="android:textColor">@color/errorForegroundColor</item>
        <item name="android:textSize">14sp</item>
    </style>
```

The styles use a default set of coulors defined in the SDK:
```xml
    <color name="primaryEditTextColor">#484545</color>
    <color name="primaryActiveEditTextColor">#000000</color>
    <color name="errorForegroundColor">#ff0303</color>

    <color name="hintColor">#d9d3d3</color>
    <color name="floatingLabelColor">#1c6edc</color>
    <color name="floatingActiveLabelColor">#0f3a73</color>
```

You can customize the add card field either overriding the colors in your app or overriding the style(s) and define your own appearence.
