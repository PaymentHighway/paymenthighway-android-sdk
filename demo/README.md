
# Payment Highway Android Demo

This example app demonstrates how to integrate Android Payment Highway SDK for add card and payment.

## Mobile Application Backend

As the secret Payment Highway credentials cannot be stored within the mobile application, a merchant backend is required for handling the authenticated communications to the service, such as fetching the card tokens and initializing the transactions. This is also where the information regarding the card tokens and payments are stored.

The bundled demo application uses a pre-made dummy merchant backend, which connects to the [Payment Highway development sandbox](https://dev.paymenthighway.io/#development-sandbox). Each merchant needs to implement this backend themselves.

For more info see the code [here]().

In client side you need to implement the BackendAdapter interface. 

The demo includes an implementation example for a `BackendAdapter` in the module `backendadapterexample`.

## Environment

Payment Highway provides a [development `sandbox`](https://dev.paymenthighway.io/#development-sandbox) and a `production` environments.

## Add a credit card

In order to add credit card and get a payment token you need a `PaymentContext`.

The demo app run in the `Sandbox` environment and therefore uses the provided [sandbox `merchant id` and `account id`](https://dev.paymenthighway.io/#development-sandbox).

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

There is a helper where you can listen for `isValidDidChange` passing a lambda:
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

SDK provides Styles for the TextInputLayout, for the EditText and for the ErrorTextAppearance.

```xml
    <style name="PHTextInputLayoutStyle" parent="Widget.Design.TextInputLayout">
        <item name="android:textColorHint">?phPrimaryForegroundColor</item>
        <item name="colorControlNormal">?phPrimaryForegroundColor</item>
        <item name="colorControlActivated">?phPrimaryForegroundColor</item>
        <item name="android:fontFamily">@font/roboto</item>
        <item name="android:background">@drawable/input_background</item>
        <item name="android:paddingRight">3dp</item>
        <item name="android:paddingLeft">3dp</item>
        <item name="android:dividerPadding">3dp</item>
        <item name="android:paddingTop">4dp</item>
    </style>

    <style name="PHEditTextStyle" parent="@android:style/Widget.EditText">
        <item name="android:textColorPrimary">@drawable/edit_text_color</item>
        <item name="android:textColorHint">?hintColor</item>
        <item name="android:fontFamily">@font/roboto</item>
        <item name="android:textStyle">normal</item>
        <item name="android:textSize">16sp</item>
        <item name="android:lineSpacingExtra">8sp</item>
     </style>

     <style name="PHErrorAppearanceStyle" parent="@android:style/TextAppearance">
         <item name="android:textColor">?errorForegroundColor</item>
         <item name="android:textSize">14sp</item>
         <item name="android:fontFamily">@font/roboto</item>
     </style>
```

The SDK defines a set of attributes that can be configured in your theme:
```xml
    <attr name="imageCardNumber" format="reference"/>
    <attr name="imageSecurityCode" format="reference"/>
    <attr name="imageExpiryDate" format="reference"/>

    <attr name="primaryBackgroundColor" format="color|reference"/>
    <attr name="secondaryBackgroundColor" format="color|reference"/>

    <attr name="highlightColor" format="color|reference"/>
    <attr name="highlightDisabledColor" format="color|reference"/>
    <attr name="highlightBackgroundColor" format="color|reference"/>

    <attr name="primaryForegroundColor" format="color|reference"/>
    <attr name="primaryActiveForegroundColor" format="color|reference"/>
    <attr name="phPrimaryForegroundColor" format="color|reference"/>
    <attr name="phPrimaryForegroundColorDark" format="color|reference"/>
    <attr name="hintColor" format="color|reference"/>
    <attr name="errorForegroundColor" format="color|reference"/>
```

SDK provides 2 themes with predefined colors.

Light theme:
```xml
    <style name="Theme.PH.Light" parent="Theme.AppCompat.Light.DarkActionBar">
        <item name="imageCardNumber">@drawable/ccnum_light</item>
        <item name="imageExpiryDate">@drawable/date_light</item>
        <item name="imageSecurityCode">@drawable/lock_light</item>

        <item name="primaryBackgroundColor">@color/primaryBackgroundColorLight</item>
        <item name="secondaryBackgroundColor">@color/secondaryBackgroundColorLight</item>

        <item name="highlightColor">@color/highlightColorLight</item>
        <item name="highlightDisabledColor">@color/highlightDisabledColorLight</item>
        <item name="highlightBackgroundColor">@color/highlightBackgroundColorLight</item>

        <item name="primaryForegroundColor">@color/primaryForegroundColorLight</item>
        <item name="primaryActiveForegroundColor">@color/primaryActiveForegroundColorLight</item>
        <item name="phPrimaryForegroundColor">@color/phPrimaryForegroundColorLight</item>
        <item name="hintColor">@color/hintColorLight</item>>
        <item name="errorForegroundColor">@color/errorForegroundColorLight</item>
    </style>
````

Dark theme:
```xml
    <style name="Theme.PH.Dark" parent="Theme.AppCompat.Light.DarkActionBar">
        <item name="imageCardNumber">@drawable/ccnum_dark</item>
        <item name="imageExpiryDate">@drawable/date_dark</item>
        <item name="imageSecurityCode">@drawable/lock_dark</item>

        <item name="primaryBackgroundColor">@color/primaryBackgroundColorDark</item>
        <item name="secondaryBackgroundColor">@color/secondaryBackgroundColorDark</item>

        <item name="highlightColor">@color/highlightColorDark</item>
        <item name="highlightDisabledColor">@color/highlightDisabledColorDark</item>
        <item name="highlightBackgroundColor">@color/highlightBackgroundColorDark</item>

        <item name="primaryForegroundColor">@color/primaryForegroundColorDark</item>
        <item name="primaryActiveForegroundColor">@color/primaryActiveForegroundColorDark</item>
        <item name="phPrimaryForegroundColor">@color/phPrimaryForegroundColorDark</item>
        <item name="hintColor">@color/hintColorDark</item>>
        <item name="errorForegroundColor">@color/errorForegroundColorDark</item>
    </style>
```

In your app theme you can use the predefined Payment Highway themes:
```xml
    <style name="AppTheme" parent="Theme.PH.Light">
        <!-- Customize your theme here. -->
        <item name="colorPrimary">?phPrimaryForegroundColor</item>
        <item name="colorPrimaryDark">?phPrimaryForegroundColor</item>
        <item name="colorAccent">@color/colorAccent</item>

    </style>
```

You can customize the add card fields either defining all the Payment Highway properties in your app theme or overriding the style(s) and define your own appearence.

You can test the Payment Highway Dark theme in the demo app just changing the AppTheme definition in styles.xml:
from:
```xml
    <style name="AppTheme" parent="Theme.PH.Light">
    ...
    </style>
````
to:
```xml
    <style name="AppTheme" parent="Theme.PH.Dark">
    ...
    </style>
````