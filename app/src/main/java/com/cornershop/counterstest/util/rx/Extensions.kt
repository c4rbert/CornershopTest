package com.cornershop.counterstest.util.rx

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment

fun Activity.color(@ColorRes color: Int) = ContextCompat.getColor(this, color)

fun Any.isNull() = this == null

fun String.getString(stringResource: Int) {
    getString(stringResource)
}

fun Activity.showView(view: View, state: Boolean) {
    view.isVisible = state
}

fun Fragment.showView(view: View, state: Boolean) {
    view.isVisible = state
}

fun View.hideKeyBoard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}