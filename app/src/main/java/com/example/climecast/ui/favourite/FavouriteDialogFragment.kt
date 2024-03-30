package com.example.climecast.ui.favourite

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class FavouriteDialogFragment : DialogFragment() {

    interface FavouriteDialogListener {
        fun onPositiveButtonClick()
    }

    private var listener: FavouriteDialogListener? = null

    fun setAlertDialogListener(listener: FavouriteDialogListener) {
        this.listener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage("Do you want to delete this location ?")
                .setPositiveButton("Delete") { dialog, id ->
                    listener?.onPositiveButtonClick()
                }
                .setNegativeButton("Cancel") { dialog, id ->
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
