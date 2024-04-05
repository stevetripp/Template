package com.example.template.util

object SmtLogger {

    private fun name(): String {
        val stack = Throwable("").fillInStackTrace()
        val trace = stack.stackTrace
        return trace[2].className.split('.').last() + "::" + trace[2].methodName + ":" + trace[2].lineNumber
    }

    fun i(msg: String = "") {
        android.util.Log.i(
            "SMT",
            """[${name()}]
            |$msg""".trimMargin()
        )
    }
}