
# Payment Highway Android Demo

This example app demonstrates how to integrate Android Payment Highway SDK for add card and payment.

## Mobile Application Backend

As the secret Payment Highway credentials cannot be stored within the mobile application, a merchant backend is required for handling the authenticated communications to the service, such as fetching the card tokens and initializing the transactions. This is also where the information regarding the card tokens and payments are stored.

The bundled demo application uses a pre-made dummy merchant backend, which connects to the [Payment Highway development sandbox](https://dev.paymenthighway.io/#development-sandbox). Each merchant needs to implement this backend themselves.

For more info see the code [here]().

In client side you need to implement the BackendAdapter interface. 

The demo includes an implementation example for a `BackendAdapter` in the module `backendadapterexample`.

## Environment

Payment Highway provide a [development `sandbox`](https://dev.paymenthighway.io/#development-sandbox) and a `production` environments.

## Add a credit card

In order to add credit card and get a payment token you need a `PaymentContext`.

The demo app run in the `Sandbox` environment therefore uses the provided [sandbox `merchant id` and `account id`](https://dev.paymenthighway.io/#development-sandbox).

Example how istantiate a `PaymentContext`:

Kotlin:
```kotlin
    val merchantId = MerchantId("test_merchantId")
    val accountId = AccountId("test")

    lateinit  var mPaymentContext: PaymentContext<TransactionToken>

    val backendAdapter = BackendAdapterExample()
    val paymentConfig = PaymentConfig(merchantId, accountId, Environment.Sandbox)
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

There is a helper where you can listen passing a lambda:
```kotlin:
    mCardInputWidget = findViewById<AddCardWidget>(R.id.add_card_widget)

    mCardInputWidget.setValidationListener {
        isValidDidChange {
            mAddCardButton.isEnabled = it
        }
    }
```

You can provide images for card text inputs implementing in your Activity the EditTextImageProvider interface:
```kotlin
    class AddCardActivity : AppCompatActivity(), EditTextImageProvider 

        mCardInputWidget = findViewById<AddCardWidget>(R.id.add_card_widget)
        mCardInputWidget.editTextImageProvider = this

        override fun imageDrawable(type: EditTextType): Int = getDrawableId(type)
    }  
````

There is a helper where you implement the provider with a lambda:
```kotlin
    mCardInputWidget = findViewById<AddCardWidget>(R.id.add_card_widget)
    mCardInputWidget.setEditTextImageDrawableProvider {
        imageDrawable { getDrawableId(it) }
    }
````

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
        <item name="android:textColorHint">@color/phPrimaryForegroundColor</item>
        <item name="colorControlNormal">@color/phPrimaryForegroundColor</item>
        <item name="colorControlActivated">@color/phPrimaryForegroundColor</item>
        <item name="android:fontFamily">@font/roboto</item>
        <item name="android:background">@drawable/input_background</item>
        <item name="android:paddingRight">3dp</item>
        <item name="android:paddingLeft">3dp</item>
        <item name="android:dividerPadding">3dp</item>
        <item name="android:paddingTop">4dp</item>
    </style>

    <style name="PHEditTextStyle" parent="@android:style/Widget.EditText">
        <item name="android:textColorPrimary">@drawable/edit_text_color</item>
        <item name="android:textColorHint">@color/hintColor</item>
        <item name="android:fontFamily">@font/roboto</item>
        <item name="android:textStyle">normal</item>
        <item name="android:textSize">16sp</item>
        <item name="android:lineSpacingExtra">8sp</item>
    </style>

    <style name="PHErrorAppearanceStyle" parent="@android:style/TextAppearance">
         <item name="android:textColor">@color/errorForegroundColor</item>
         <item name="android:textSize">14sp</item>
         <item name="android:fontFamily">@font/roboto</item>
    </style>
```

The styles use a default set of colors defined in the SDK:
```xml
    <color name="primaryBackgroundColor">@android:color/transparent</color>
    <color name="secondaryBackgroundColor">#e0e0e0</color>

    <color name="highlightColor">@color/phPrimaryForegroundColor</color>
    <color name="highlightDisabledColor">#997c7c7c</color>
    <color name="highlightBackgroundColor">@color/secondaryBackgroundColor</color>

    <color name="primaryForegroundColor">#99000000</color>
    <color name="primaryActiveForegroundColor">#de000000</color>
    <color name="phPrimaryForegroundColor">#9d00ba</color>
    <color name="phPrimaryForegroundColorDark">#d261e7</color>
    <color name="hintColor">#ababab</color>
    <color name="errorForegroundColor">#ff0000</color>
```

You can customize the add card field either overriding the colors in your app or overriding the style(s) and define your own appearence.

In the demo there is a definition of a dark theme as a example. You can try it just uncommenting the definition of the colors in the file color.xml. You need set the variable `val themeDark = true` in the file AddCardActivity; this is needed to return different images for the card inputs.
