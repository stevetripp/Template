@file:Suppress("unused")

package org.lds.mobile.ui.ext

import android.content.Context
import android.content.ContextWrapper
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.annotation.StringRes

// Toast
fun Context.toast(text: CharSequence, duration: Int) = Toast.makeText(this, text, duration).show()
fun Context.toast(@StringRes stringId: Int, duration: Int) = Toast.makeText(this, stringId, duration).show()
fun Context.toastLong(@StringRes stringId: Int) = toast(stringId, Toast.LENGTH_LONG)
fun Context.toastLong(text: CharSequence) = toast(text, Toast.LENGTH_LONG)
fun Context.toastShort(@StringRes stringId: Int) = toast(stringId, Toast.LENGTH_SHORT)
fun Context.toastShort(text: CharSequence) = toast(text, Toast.LENGTH_SHORT)

fun Context.requireActivity(): ComponentActivity = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.requireActivity()
    else -> error("No Activity Found")
}