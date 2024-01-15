package com.example.hiltmvvm.customview

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import com.example.hiltmvvm.R
import com.example.hiltmvvm.databinding.BottomSheetLocationSortbyBinding
import com.example.hiltmvvm.util.Enum
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class LocationSortByBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetLocationSortbyBinding? = null
    private val binding get() = _binding!!

    private var selectedSort: String? = null
    private var appliedSort: String? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        setStyle(STYLE_NORMAL, R.style.MyBottomSheet)
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetLocationSortbyBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()

        binding.btnApply.setOnClickListener {

        }

        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                binding.rbDistance.id -> {
                    selectedSort = Enum.SortBy.DISTANCE.type
                }

                binding.rbRating.id -> {
                    selectedSort = Enum.SortBy.RATING.type
                }

                binding.rbReviewCount.id -> {
                    selectedSort = Enum.SortBy.REVIEW_COUNT.type
                }

                binding.rbBestMatch.id -> {
                    selectedSort = Enum.SortBy.BEST_MATCH.type
                }
            }
        }



        binding.btnClose.setOnClickListener {
            dismiss()
        }
    }

    private fun initViews() {
        binding.sbRadiusSelector.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar,
                progressValue: Int,
                fromUser: Boolean
            ) {
//                binding.tvRadius.text = "$progressValue mi"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // Do something
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // Do something
            }
        })
    }
}