package com.github.ipcjs.screenshottile.util

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Handler
import android.os.Looper

inline fun Any?.returnUnit(): Unit = Unit

inline fun <reified T> Any?.returnValue(value: T): T = value

inline fun pass() {}

object ProgressDialog {
    fun show(
            context: Context,
            title: CharSequence? = null,
            titleId: Int = 0,
            message: CharSequence? = null,
            messageId: Int = 0,
            indeterminate: Boolean = false,
            cancelable: Boolean = false,
            cancelListener: DialogInterface.OnCancelListener? = null
    ): ProgressDialog {
        return ProgressDialog.show(context, context.text(titleId, title), context.text(messageId, message), indeterminate, cancelable, cancelListener)
    }
}

object AlertDialog {
    fun show(
            context: Context,
            title: CharSequence? = null,
            titleId: Int = 0,
            message: CharSequence? = null,
            messageId: Int = 0,
            positiveText: CharSequence? = null,
            positiveTextId: Int = 0,
            negativeText: CharSequence? = null,
            negativeTextId: Int = 0,
            neutralText: CharSequence? = null,
            neutralTextId: Int = 0,
            cancelable: Boolean = false,
            clickListener: DialogInterface.OnClickListener? = null
    ): AlertDialog {
        val dialog = AlertDialog.Builder(context)
                .setTitle(context.text(titleId, title))
                .setMessage(context.text(messageId, message))
                .setCancelable(cancelable)
                .setPositiveButton(context.text(positiveTextId, positiveText), clickListener)
                .setNegativeButton(context.text(negativeTextId, negativeText), clickListener)
                .setNeutralButton(context.text(neutralTextId, neutralText), clickListener)
                .create()
        dialog.show()
        return dialog
    }
}

private fun Context.text(textId: Int, text: CharSequence?): CharSequence? {
    return if (textId != 0) {
        check(text == null) { "设置了textId(${resources.getResourceName(textId)})时, 不要设置text($text)" }
        getText(textId)
    } else {
        text
    }
}

private val uiHandler = Handler(Looper.getMainLooper())
fun runOnUiThread(block: () -> Unit) {
    if (Looper.myLooper() == Looper.getMainLooper()) {
        block()
    } else {
        uiHandler.post(block)
    }
}