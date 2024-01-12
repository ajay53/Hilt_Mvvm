package com.example.hiltmvvm.customview

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.hiltmvvm.R
import com.example.hiltmvvm.databinding.DialogPermissionRequestBinding

class PermissionRequestDialog() : DialogFragment() {

    constructor(
        title: String,
        desc: String,
        positiveText: String,
        negativeText: String,
        iconId: Int,
        listener: PermissionRequestDialogClickListener
    ) : this() {
        this.title = title
        this.desc = desc
        this.positiveText = positiveText
        this.negativeText = negativeText
        this.iconId = iconId
        this.listener = listener
    }

    private lateinit var title: String
    private lateinit var desc: String
    private lateinit var positiveText: String
    private lateinit var negativeText: String
    private var iconId: Int = R.drawable.ic_restaurant
    private lateinit var listener: PermissionRequestDialogClickListener

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
        val dialog =
            AlertDialog.Builder(requireActivity(), R.style.CustomAlertDialog)
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

