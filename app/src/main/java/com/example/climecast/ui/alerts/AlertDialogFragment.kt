package com.example.climecast.ui.alerts

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class AlertDialogFragment : DialogFragment() {

    interface AlertDialogListener {
        fun onPositiveButtonClick()
    }

    private var listener: AlertDialogListener? = null

    fun setAlertDialogListener(listener: AlertDialogListener) {
        this.listener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage("Do you want to delete this alarm ?")
                .setPositiveButton("Delete") { dialog, id ->
                    listener?.onPositiveButtonClick()
                }
                .setNegativeButton("Cancel") { dialog, id ->
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
