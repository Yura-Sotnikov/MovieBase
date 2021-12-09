package com.sym.moviebase

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.TextView
import com.google.android.material.button.MaterialButton

class QuitDialog(context: Context, private val clickListener: QuitDialogClickListeners) :
    Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.quit_dialog)

        findViewById<MaterialButton>(R.id.btn_Cancel).setOnClickListener { dismiss() }
        findViewById<MaterialButton>(R.id.btn_OK).setOnClickListener {
            dismiss()
            clickListener.onOKClick()
        }

    }

    interface QuitDialogClickListeners {
        fun onOKClick()
    }
}