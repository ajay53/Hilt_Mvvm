package com.example.hiltmvvm.customview

import android.app.Activity
import android.app.Dialog
import android.content.res.Resources
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.example.hiltmvvm.databinding.DialogPermissionRequestBinding
import com.example.hiltmvvm.util.Util
import com.google.android.gms.location.R

class PermissionRequestDialog(
    private val title: String,
    private val desc: String,
    private val positiveText: String,
    private val negativeText: String,
    private val iconId: Int,
    private val activity: FragmentActivity
) : DialogFragment(), View.OnClickListener {

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

       /* val layoutParams: WindowManager.LayoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window?.attributes)
        val dialogWidth: Int = (Util.getScreenWidth(activity) * 0.6f).toInt()
        layoutParams.width = dialogWidth
        dialog.window?.attributes = layoutParams*/

//        binding.tvTitle.text = title
        binding.ivCross.setOnClickListener(this)

        return dialog
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            binding.ivCross.id -> {
                this.dismiss()
                Toast.makeText(activity, "Cross Clicked", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun DialogFragment.setWidthPercent(percentage: Int) {
        val percent = percentage.toFloat() / 100
        val dm = Resources.getSystem().displayMetrics
        val rect = dm.run { Rect(0, 0, widthPixels, heightPixels) }
        val percentWidth = rect.width() * percent
        dialog?.window?.setLayout(percentWidth.toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    fun DialogFragment.setFullScreen() {
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}