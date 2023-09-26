package com.example.hiltmvvm.customview

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        binding.btnApply.setOnClickListener{

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
}