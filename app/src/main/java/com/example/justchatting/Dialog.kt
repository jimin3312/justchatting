package com.example.justchatting

import android.content.Context
import android.content.DialogInterface
import android.view.Gravity
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class Dialog {
    companion object{
        fun create(context: Context, message: String, buttonName: String) {
            val alertDialog = AlertDialog.Builder(context).create()
            alertDialog.setMessage(message)
            alertDialog.setButton(
                AlertDialog.BUTTON_POSITIVE,
                buttonName
            ) { dialog, _ ->
                dialog.dismiss()
            }
            alertDialog.show()
            val positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
            val layoutParams = positiveButton.layoutParams as LinearLayout.LayoutParams
            layoutParams.weight = 1.0f
            layoutParams.gravity = Gravity.CENTER
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).layoutParams = layoutParams
        }
    }
}