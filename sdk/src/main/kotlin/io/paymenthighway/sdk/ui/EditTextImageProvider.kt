package io.paymenthighway.sdk.ui

import io.paymenthighway.sdk.CardBrand

enum class EditTextType {
    CARD_NUMBER, EXPIRY_DATE, SECURITY_CODE
}

/**
 * Interface to provide image for a card text input
 */
interface EditTextImageProvider {

    /**
     * Get the image drawable id for a card text input
     *
     * @param type which card text input
     * @return drawable id; 0 for no image
     */
    fun imageDrawable(type: EditTextType): Int

}

private typealias ImageDrawableProvider = (type: EditTextType) -> Int

class EditTextImageProviderHelper: EditTextImageProvider {

    private var imageDrawableProvider: ImageDrawableProvider? = null

    fun imageDrawable(imageDrawableProvider: ImageDrawableProvider) {
        this.imageDrawableProvider = imageDrawableProvider
    }

    override fun imageDrawable(type: EditTextType): Int {
        return this.imageDrawableProvider?.invoke(type) ?: 0
    }
}

fun AddCardWidget.setEditTextImageDrawableProvider(init: EditTextImageProviderHelper.() -> Unit) {
    val provider = EditTextImageProviderHelper()
    provider.init()
    this.editTextImageProvider = provider
}