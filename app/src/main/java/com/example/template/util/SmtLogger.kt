package com.example.template.util

import android.annotation.SuppressLint

object SmtLogger {

    @Suppress("UnusedParameter")
    private fun name(index: Int = 3): String {
        val stack = Throwable("").fillInStackTrace()
        val trace = stack.stackTrace
        return trace[3].className.split('.').last() + "::" + trace[3].methodName + ":" + trace[3].lineNumber
    }

    @SuppressLint("LogNotTimber")
    fun i(msg: String = "") {
        android.util.Log.i(
            "SMT",
            """[${name()}]
            |$msg""".trimMargin()
        )
    }
}