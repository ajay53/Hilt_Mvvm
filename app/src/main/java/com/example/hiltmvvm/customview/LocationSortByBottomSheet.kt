package com.example.hiltmvvm.customview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.hiltmvvm.databinding.BottomSheetLocationSortbyBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class LocationSortByBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetLocationSortbyBinding? = null
    private val binding get() = _binding!!

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

        //init UI
        //set onclick listener
        binding.tv1.setOnClickListener {
            Toast.makeText(requireContext(), "Text 1", Toast.LENGTH_SHORT).show()
        }

    }
}