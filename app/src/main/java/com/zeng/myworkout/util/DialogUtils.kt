package com.zeng.myworkout.util

import android.content.Context
import androidx.appcompat.app.AlertDialog

object DialogUtils {

    fun openValidationDialog(
        context: Context,
        message: String = "Validation",
        positive: String = "Yes",
        negative: String = "Cancel",
        positiveFun: (() -> Unit)? = null,
        negativeFun: (() -> Unit)? = null
    ) {
        val dialog = AlertDialog.Builder(context)
            .setMessage(message)
            .setPositiveButton(positive) { _, _ -> positiveFun?.invoke() }
            .setNegativeButton(negative) {  _, _ -> negativeFun?.invoke() }
            .create()

        dialog.show()
    }
}
