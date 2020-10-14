package com.techbay.techbayportal.utils.extensions

import android.widget.EditText

fun EditText.getString(): String {
    return this.text.toString().trim()
}