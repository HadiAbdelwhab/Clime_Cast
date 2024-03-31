package com.example.climecast.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.climecast.R

class ConfirmationDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.order_confirmation))
            .setPositiveButton(getString(R.string.ok)) { _,_ -> }
            .create()

    companion object {
        const val TAG = "PurchaseConfirmationDialog"
    }
}
