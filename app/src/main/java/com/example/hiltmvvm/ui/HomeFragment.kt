package com.example.hiltmvvm.ui

import android.Manifest
import android.content.Intent
import com.example.hiltmvvm.R
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.HandlerThread
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.hiltmvvm.adapter.FilterCategoryInteraction
import com.example.hiltmvvm.customview.LocationSortByBottomSheet
import com.example.hiltmvvm.customview.PermissionRequestDialog
import com.example.hiltmvvm.customview.PermissionRequestDialogClickListener
import com.example.hiltmvvm.databinding.DialogLocationRequestBinding
import com.example.hiltmvvm.databinding.FragmentHomeBinding
import com.example.hiltmvvm.model.FilterCategoryObject
import com.example.hiltmvvm.util.Constants
import com.example.hiltmvvm.util.Util
import com.example.hiltmvvm.util.Enum
import com.example.hiltmvvm.viewmodel.HomeViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

class HomeFragment : Fragment(), View.OnClickListener,
    FilterCategoryInteraction, PermissionRequestDialogClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var radius: Int = 100
    private var resetData: Boolean = false
    private val locHandler = HandlerThread("location")
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private var lat: Double = 0.0
    private var lon: Double = 0.0
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews() {
        binding.tvSearch.setOnClickListener(this)
        binding.tvRestaurants.setOnClickListener(this)
        binding.fabSetSelection.setOnClickListener(this)

        /*binding.sbRadiusSelector.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {

                //return if permission is not granted
                if (!arePermissionsEnabled()) {
                    binding.sbRadiusSelector.progress = 0
                    return
                }

                //keeping the min radius as 100
                radius = when (p1) {
                    0 -> {
                        100
                    }

                    else -> {
                        250 * p1
                    }
                }

                //using formatter to remove zero after decimal point
                val decimalFormat = DecimalFormat("0.##")
                binding.tvRadiusVal.text =
                    if (radius >= 1000) {
                        "${decimalFormat.format(radius / 1000f)} KM"
                    } else {
                        "$radius M"
                    }

                //call api
                binding.progressBar.show()
                binding.ivNoRestaurant.visibility = View.GONE
                binding.tvNoRestaurant.visibility = View.GONE
                resetData = true
                isLoading = true
                isLastPage = false
                //using location as per switch
                val searchBusiness = if (binding.swLocation.isChecked) {
                    SearchBusiness(
                        40.730610,
                        -73.935242,
                        radius,
                        Enum.SortBy.DISTANCE.type,
                        Constants.PAGE_LIMIT,
                        0
                    )
                } else {
                    SearchBusiness(
                        lat, lon, radius, Enum.SortBy.DISTANCE.type, Constants.PAGE_LIMIT,
                        0
                    )
                }

                viewModel.setSearchBusiness(searchBusiness)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                //ask for permissions if not granted
                if (!arePermissionsEnabled()) {
                    askPermissions()
                    binding.sbRadiusSelector.progress = 0
                }
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        })*/

        /*binding.rvFilterCategory.apply {
            if (!this@HomeFragment::filterRecyclerAdapter.isInitialized) {
                filterRecyclerAdapter = FilterCategoryRecyclerAdapter(this@HomeFragment)
            }
            adapter = filterRecyclerAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }*/
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            binding.tvSearch.id -> {
                LocationSortByBottomSheet().show(childFragmentManager, null)
            }

            binding.tvRestaurants.id -> {
                PermissionRequestDialog(
                    "",
                    "",
                    "",
                    "",
                    123,
                    object : PermissionRequestDialogClickListener {
                        override fun onCrossClicked() {
                            TODO("Not yet implemented")
                        }

                        override fun onPositiveClicked() {
                            TODO("Not yet implemented")
                        }

                        override fun onNegativeClicked() {
                            TODO("Not yet implemented")
                        }

                    }).show(
                    childFragmentManager,
                    null
                )
            }

            binding.fabSetSelection.id -> LocationSortByBottomSheet().show(
                childFragmentManager,
                null
            )

        }
    }

    override fun onFilterClicked(position: Int, item: FilterCategoryObject) {
        TODO("Not yet implemented")
    }

    private fun askPermissions() {
        if (!Util.isGpsEnabled(requireContext())) {
            showLocationPermissionDialog(com.example.hiltmvvm.util.Enum.Permission.GPS)
        }
        if (!Util.hasLocationPermission(requireContext())) {
            showLocationPermissionDialog(com.example.hiltmvvm.util.Enum.Permission.LOCATION)
        }
    }

    private fun arePermissionsEnabled(): Boolean {
        return (Util.isGpsEnabled(requireContext()) && Util.hasLocationPermission(
            requireContext()
        ))
    }

    private fun showLocationPermissionDialog(permission: com.example.hiltmvvm.util.Enum.Permission) {
        val alertBinding: DialogLocationRequestBinding =
            DialogLocationRequestBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
            .create()
        builder.setView(alertBinding.root)

        when (permission) {
            com.example.hiltmvvm.util.Enum.Permission.GPS -> {
                alertBinding.tvTitle.text = getString(R.string.gps_permission_title)
                alertBinding.tvDesc.text = getString(R.string.gps_permission_desc)
            }

            Enum.Permission.LOCATION -> {
                alertBinding.tvTitle.text = getString(R.string.location_permission_title)
                alertBinding.tvDesc.text = getString(R.string.location_permission_desc)
            }
        }

        alertBinding.tvConfirm.setOnClickListener {
            builder.dismiss()
//            Toast.makeText(applicationContext, "confirm", Toast.LENGTH_SHORT).show()
            when (permission) {
                Enum.Permission.GPS -> {
                    gpsReqLauncher.launch(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }

                Enum.Permission.LOCATION -> {
                    permReqLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                    )
                }
            }
        }
        alertBinding.tvCancel.setOnClickListener {
            builder.dismiss()
        }
        alertBinding.ivCross.setOnClickListener {
            builder.dismiss()
        }

        builder.setCanceledOnTouchOutside(false)
        builder.show()
        val layoutParams: WindowManager.LayoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(builder.window?.attributes)
        val dialogWidth: Int = (Util.getScreenWidth(requireActivity()) * 0.8f).toInt()
        layoutParams.width = dialogWidth
        builder.window?.attributes = layoutParams
    }

    private val permReqLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach { entry ->
                if (entry.key == Manifest.permission.ACCESS_FINE_LOCATION) {
                    if (entry.value) {
                        if (arePermissionsEnabled()) {
                            startLocationUpdates()
                        }
                    } else {
//                        binding.sbRadiusSelector.progress = 0
                        viewModel.setSeekbarProgress(0)
                        showLocationPermissionDialog(Enum.Permission.LOCATION)
                    }
                }
            }
        }

    private val gpsReqLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            //check for gps access
            if (arePermissionsEnabled()) {
                startLocationUpdates()
            }
        }

    private fun createLocationRequest() {
        locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            Constants.UPDATE_INTERVAL_IN_MILLISECONDS
        ).build()

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        locationCallback = object : LocationCallback() {
            //NOT EXECUTING WHEN APP IS IN BACKGROUND
            //added foreground service for this
            override fun onLocationResult(locationResult: LocationResult) {

                val currLocation: Location? = locationResult.lastLocation
                currLocation?.let {
                    lat = currLocation.latitude
                    lon = currLocation.longitude
                    /*Log.d(
                        TAG,
                        "onLocationResult: lat: ${currLocation.latitude} || Lon: ${currLocation.longitude}"
                    )*/
                }
            }
        }
    }

    private fun startLocationUpdates() {
        if (!locHandler.isAlive) {
            locHandler.start()
        }

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            locHandler.looper
        )
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCrossClicked() {
        TODO("Not yet implemented")
    }

    override fun onPositiveClicked() {
        TODO("Not yet implemented")
    }

    override fun onNegativeClicked() {
        TODO("Not yet implemented")
    }
}