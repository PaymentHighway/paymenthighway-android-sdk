package io.paymenthighway.sdk.ui

import android.content.Context
import android.graphics.Rect
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import io.paymenthighway.sdk.R
import io.paymenthighway.sdk.R.color.*

open class EditText : TextInputEditText {

    var format: (String) -> String = { it }
    var validate: (String) -> Boolean = { false }
    var editTextValidationListener: ValidationListener? = null
    var hintText: String? = null

    var textLayout: TextInputLayout? = null
    var errorText: String? = null

    var isValid: Boolean = false
        set(value: Boolean) {
            field = value
            editTextValidationListener?.isValidDidChange(value)
        }

    constructor(context: Context?) : super(context) {}

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                //no action
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                //no action
            }

            override fun afterTextChanged(s: Editable) {
                resetError()
                val formatted = format(s.toString())
                if (formatted != s.toString()) {
                    setText(formatted)
                    setSelection(formatted.length)
                }

                if (validate(formatted) != isValid) {
                    isValid = !isValid
                }
            }
        })
    }

    private fun resetError() {
        textLayout?.isErrorEnabled = false
        textLayout?.error = null
    }

    private var showError: Boolean = false
        get() {
            if (text == null || text!!.length == 0 ) return false
            return !isValid
        }

    private fun checkIfError() {
        if (showError) {
            textLayout?.isErrorEnabled = true
            textLayout?.error = errorText
        }
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        if (hintText != null) {
            setHint(if (focused) hintText else "")
        }
        checkIfError()
    }
}