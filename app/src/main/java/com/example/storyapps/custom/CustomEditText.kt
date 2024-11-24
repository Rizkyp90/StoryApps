package com.example.storyapps.custom

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import com.example.storyapps.R

class CustomEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatEditText(context, attrs, defStyleAttr) {

    private var errorTextView: TextView? = null

    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateInput()
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    fun setErrorTextView(textView: TextView) {
        errorTextView = textView
    }

    private fun validateInput() {
        val inputText = text.toString().trim()
        when (id) {
            R.id.ed_register_name -> {
                if (inputText.isEmpty()) {
                    errorTextView?.text = "Nama harus diisi"
                } else {
                    errorTextView?.text = null
                }
            }
            R.id.ed_register_email, R.id.login_email -> {
                if (inputText.isEmpty()) {
                    errorTextView?.text = "Email harus diisi"
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(inputText).matches()) {
                    errorTextView?.text = "Format email tidak valid"
                } else {
                    errorTextView?.text = null
                }
            }
            R.id.ed_register_password, R.id.login_password -> {
                if (inputText.isEmpty()) {
                    errorTextView?.text = "Password harus diisi"
                } else if (inputText.length < 8) {
                    errorTextView?.text = "Password minimal 8 karakter"
                } else if (!inputText.matches(Regex(".*[a-zA-Z].*"))) {
                    errorTextView?.text = "Password harus mengandung huruf"
                } else if (!inputText.matches(Regex(".*\\d.*"))) {
                    errorTextView?.text = "Password harus mengandung angka"
                } else {
                    errorTextView?.text = null
                }
            }
        }
    }
}
