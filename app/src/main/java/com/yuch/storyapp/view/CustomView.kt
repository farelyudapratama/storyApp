package com.yuch.storyapp.view

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.yuch.storyapp.R

class EditTextPassword : AppCompatEditText{
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not Yet Implemented
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null && s.length < 8) {
                    setError(resources.getString(R.string.error_password_minimal),null)
                } else {
                    error = null
                }
            }

            override fun afterTextChanged(s: android.text.Editable?) {
                // Not Yet Implemented
            }
        })
    }
}

class EditTextEmail : AppCompatEditText{
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not Yet Implemented
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val email = text.toString()
                val isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
                if (!isEmailValid) {
                    setError(resources.getString(R.string.error_email_invalid),null)
                } else {
                    error = null
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // Not Yet Implemented
            }

        })
    }
}