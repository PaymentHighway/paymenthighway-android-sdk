package io.paymenthighway.sdk.ui

import android.content.Context
import android.graphics.Rect
import android.support.design.widget.TextInputEditText
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import io.paymenthighway.sdk.R.color.*

open class EditText : TextInputEditText { //, View.OnFocusChangeListener {

    var format: (String) -> String = { it }
    var validate: (String) -> Boolean = { false }
    var editTextValidationListener: ValidationListener? = null
    var hintText: String? = null

    var isValid: Boolean = false
        set(value: Boolean) {
            field = value
            editTextValidationListener?.isValidDidChange(value)
        }

    constructor(context: Context?) : super(context) {}

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    init {
        setHintTextColor(ContextCompat.getColor(context, hintColor))
        setTextColor(false, false)
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                //no action
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                //no action
            }

            override fun afterTextChanged(s: Editable) {
                val formatted = format(s.toString())
                if (formatted != s.toString()) {
                    setText(formatted)
                    setSelection(formatted.length)
                }
                val vv = validate(formatted)
                if (validate(formatted) != isValid) {
                    isValid = !isValid
                    setTextColor(isValid, hasFocus())
                }
            }
        })
    }


    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        setTextColor(isValid, focused)
        if (hintText != null) {
            setHint(if (focused) hintText else "")
        }
    }

    fun setTextColor(isValid: Boolean, isActive: Boolean) {
        var newColor: Int
        if (isValid) {
            newColor = if (isActive) primaryActiveEditTextColor else primaryEditTextColor
        } else {
            newColor = if (isActive) errorActiveForegroundColor else errorForegroundColor
        }
        setTextColor(ContextCompat.getColor(context, newColor))
    }
}