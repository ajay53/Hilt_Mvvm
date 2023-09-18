package com.example.hiltmvvm.customview

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.example.hiltmvvm.databinding.DialogPermissionRequestBinding

class PermissionRequestDialog(
    private val activity: FragmentActivity,
    private val title: String,
    private val desc: String,
    private val positiveText: String,
    private val negativeText: String,
    private val iconId: Int,
    private val listener: PermissionRequestDialogClickListener
) : DialogFragment() {

    private var _binding: DialogPermissionRequestBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onResume() {
        super.onResume()
        setWidthPercent(70)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding =
            DialogPermissionRequestBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(activity, com.example.hiltmvvm.R.style.CustomAlertDialog)
            .create()
        dialog.setView(binding.root)
        dialog.setCanceledOnTouchOutside(false)

//        binding.tvTitle.text = title
        binding.ivCross.setOnClickListener {
            listener.onCrossClicked()
        }
        binding.tvConfirm.setOnClickListener {
            listener.onPositiveClicked()
        }
        binding.tvCancel.setOnClickListener {
            listener.onNegativeClicked()
        }

        return dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

interface PermissionRequestDialogClickListener {
    fun onCrossClicked()
    fun onPositiveClicked()
    fun onNegativeClicked()
}

