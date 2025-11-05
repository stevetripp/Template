package com.example.template.util

import android.util.Log
import kotlin.system.measureTimeMillis
import kotlinx.coroutines.runBlocking

object SmtLogger {

    private fun name(index: Int): String {
        val stack = Throwable("").fillInStackTrace()
        return stack.stackTrace[index].toString()
    }

    fun i(msg: String? = null, timedBlock: (suspend () -> Unit)? = null) {
        val duration = measureTimeMillis { runBlocking { timedBlock?.invoke() } }
        val sb = StringBuilder().apply {
            append("[${name(3)}]")
            if (timedBlock != null) appendLine(" ($duration ms)") else appendLine()
            msg?.also { appendLine(it) }
        }

        Log.i("SMT", sb.toString())
    }


    fun <T> i(msg: String? = null, timedBlock: (suspend () -> T?)? = null): T? {
        var result: T? = null
        val duration = measureTimeMillis { result = runBlocking { timedBlock?.invoke() } }

        val sb = StringBuilder().apply {
            append("[${name(2)}]")
            if (timedBlock != null) appendLine(" ($duration ms)") else appendLine()
            msg?.also { appendLine(it) }
        }

        Log.i("SMT", sb.toString())

        return result
    }
}