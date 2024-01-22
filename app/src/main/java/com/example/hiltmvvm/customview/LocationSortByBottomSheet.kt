package com.example.hiltmvvm.customview

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.activityViewModels
import com.example.hiltmvvm.R
import com.example.hiltmvvm.databinding.BottomSheetLocationSortbyBinding
import com.example.hiltmvvm.util.Constants
import com.example.hiltmvvm.util.Enum
import com.example.hiltmvvm.viewmodel.MainViewModel
import com.example.hiltmvvm.model.SearchBusiness
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.DecimalFormat

class LocationSortByBottomSheet(private val listener: OnLocationBottomSheetAction) :
    BottomSheetDialogFragment(R.layout.bottom_sheet_location_sortby) {

    private var _binding: BottomSheetLocationSortbyBinding? = null
    private val binding get() = _binding!!

    private var selectedSort: String? = null
    private val viewModel: MainViewModel by activityViewModels()

    /*constructor(viewModel: HomeViewModel) : this() {
        this.viewModel = viewModel
        this.viewModel by activityViewModels<> {  }
    }*/

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

//        initViews()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews() {
        binding.btnApply.setOnClickListener {
            selectedSort?.let {
                viewModel.activeSort = it
            }
        }

        binding.swLocation.setOnCheckedChangeListener { _, isChecked ->
            //resetting views on switch change
            binding.tvLocationSwitch.text = if (isChecked) {
                "Location: NYC"
            } else {
                "Location: Current"
            }
            if (this::businesses.isInitialized) {
//                businesses.clear()
                businesses = mutableListOf()
            }

            if (this::recyclerAdapterNew.isInitialized) {
                recyclerAdapterNew.submitList(businesses, binding.rvRestaurants)
            }

            //OG Code
            /*if (this::recyclerAdapter.isInitialized) {
                recyclerAdapter.notifyDataSetChanged()
            }*/
            resetData = true
            isLoading = false
            isLastPage = false
            binding.sbRadiusSelector.progress = 0
//            binding.ivNoRestaurant.visibility = View.VISIBLE
//            binding.tvNoRestaurant.visibility = View.VISIBLE
            viewModel.cancelJobs()
            binding.progressBar.hide()
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

        binding.sbRadiusSelector.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {

                //return if permission is not granted
                if (!viewModel.arePermissionsEnabled(requireContext())) {
                    binding.sbRadiusSelector.progress = 0
                    return
                }

                //keeping the min radius as 100
                viewModel.radius = when (p1) {
                    0 -> {
                        100
                    }

                    else -> {
                        250 * p1
                    }
                }
                viewModel.radiusPosition = p1

                //using formatter to remove zero after decimal point
                val decimalFormat = DecimalFormat("0.##")
                binding.tvRadiusVal.text =
                    if (viewModel.radius >= 1000) {
                        "${decimalFormat.format(viewModel.radius / 1000f)} KM"
                    } else {
                        "${viewModel.radius} M"
                    }

                //call api
//                binding.progressBar.show()
//                binding.ivNoRestaurant.visibility = View.GONE
//                binding.tvNoRestaurant.visibility = View.GONE
//                resetData = true
//                isLoading = true
//                isLastPage = false
                //using location as per switch
                val searchBusiness = if (binding.swLocation.isChecked) {
                    SearchBusiness(
                        Constants.LAT_US,
                        Constants.LON_US,
                        viewModel.radius,
                        Enum.SortBy.DISTANCE.type,
                        Constants.PAGE_LIMIT,
                        0
                    )
                } else {
                    SearchBusiness(
                        viewModel.lat,
                        viewModel.lon,
                        viewModel.radius,
                        Enum.SortBy.DISTANCE.type,
                        Constants.PAGE_LIMIT,
                        0
                    )
                }

                viewModel.setSearchBusiness(searchBusiness)
                dismiss()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                //ask for permissions if not granted
                if (!viewModel.arePermissionsEnabled(requireContext())) {
//                    askPermissions()
                    listener.onPermissionsDenied()
                    binding.sbRadiusSelector.progress = 0
                }
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        })

        binding.btnClose.setOnClickListener {
            selectedSort = null
            dismiss()
        }
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        selectedSort = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

interface OnLocationBottomSheetAction {
    fun onPermissionsDenied()
    fun onSwitchClicked()
}