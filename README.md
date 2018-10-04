# Overview

This is the [Payment Highway](https://www.paymenthighway.io) Android SDK.

## Requirements

* You have an existing Android project supporting Android SDK Version 14 or later
* Valid Payment Highway `account id` and `merchant id`
* Custom-build Mobile Application Backend that returns session id and handles token with token id

## Mobile Application Backend

You will need to have a custom mobile application backend that handles session id and token management.

See the backendadapterexample module included in the SDK.

## License
MIT

# Install

```kotlin
repositories {
    jcenter()
}

dependencies {
     implementation 'io.paymenthighway.sdk:paymenthighway-android-sdk:2.0.0'
}
```

# Use

See the [Demo Project](https://github.com/PaymentHighway/paymenthighway-android-sdk/tree/master/demo) included how to use the sdk.
