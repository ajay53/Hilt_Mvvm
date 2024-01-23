package com.example.hiltmvvm.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.HandlerThread
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.hiltmvvm.R
import com.example.hiltmvvm.adapter.BusinessRecyclerAdapter
import com.example.hiltmvvm.adapter.FilterCategoryInteraction
import com.example.hiltmvvm.customview.LocationSortByBottomSheet
import com.example.hiltmvvm.customview.OnLocationBottomSheetAction
import com.example.hiltmvvm.customview.PermissionRequestDialog
import com.example.hiltmvvm.customview.PermissionRequestDialogClickListener
import com.example.hiltmvvm.databinding.FragmentHomeBinding
import com.example.hiltmvvm.model.FilterCategoryObject
import com.example.hiltmvvm.util.Constants
import com.example.hiltmvvm.util.Util
import com.example.hiltmvvm.util.Enum
import com.example.hiltmvvm.viewmodel.HomeViewModel
import com.example.hiltmvvm.viewmodel.MainViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

//import com.google.android.gms.location.R


class HomeFragment : Fragment(R.layout.fragment_home), View.OnClickListener,
    FilterCategoryInteraction, PermissionRequestDialogClickListener, OnLocationBottomSheetAction {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerAdapter: BusinessRecyclerAdapter
    private var radius: Int = 100
    private var resetData: Boolean = false
    private val locHandler = HandlerThread("location")
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest

    //    private var lat: Double = 0.0
//    private var lon: Double = 0.0
    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        askPermissions()
        createLocationRequest()
    }

    override fun onResume() {
        super.onResume()
        if (viewModel.arePermissionsEnabled(requireContext())) {
            startLocationUpdates()
        }
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    private fun initViews() {
        binding.tvSearch.setOnClickListener(this)
        binding.tvRestaurants.setOnClickListener(this)

        viewModel.businessServiceClass.observe(viewLifecycleOwner){business->
            binding.progressBar.hide()

            //handling response code
            if (business == null || business.code != 200) {
                if (business != null && business.message.isNotBlank()) {
                    Toast.makeText(requireContext(), business.message, Toast.LENGTH_SHORT)
                        .show()
                }
//                binding.ivNoRestaurant.visibility = View.VISIBLE
//                binding.tvNoRestaurant.visibility = View.VISIBLE
            } else {
                updateUI(business)
            }
        }

        binding.rvBusiness.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                //if at bottom
                if (!viewModel.arePermissionsEnabled(requireContext())) {
                    askPermissions()
                    return
                }
            }
        })

        /*binding.rvFilterCategory.apply {
            if (!this@HomeFragment::filterRecyclerAdapter.isInitialized) {
                filterRecyclerAdapter = FilterCategoryRecyclerAdapter(this@HomeFragment)
            }
            adapter = filterRecyclerAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }*/
    }

    private fun updateUI(businessServiceClass: BusinessesServiceClass) {


    }


    override fun onClick(p0: View?) {
        when (p0?.id) {
            binding.tvSearch.id -> {
                LocationSortByBottomSheet(listener = this).show(childFragmentManager, null)
            }

            binding.tvRestaurants.id -> {

            }
        }
    }

    override fun onPermissionsDenied() {
        askPermissions()
    }

    override fun onSwitchClicked() {

    }

    override fun onFilterClicked(position: Int, item: FilterCategoryObject) {
        TODO("Not yet implemented")
    }

    private fun askPermissions() {
        if (!Util.isGpsEnabled(requireContext())) {
            showLocationPermissionDialog(Enum.Permission.GPS)
        }
        if (!Util.hasLocationPermission(requireContext())) {
            showLocationPermissionDialog(Enum.Permission.LOCATION)
        }
    }

    private val permReqLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach { entry ->
                if (entry.key == Manifest.permission.ACCESS_FINE_LOCATION) {
                    if (entry.value) {
                        if (viewModel.arePermissionsEnabled(requireContext())) {
                            startLocationUpdates()
                        }
                    } else {
//                        binding.sbRadiusSelector.progress = 0
                        showLocationPermissionDialog(Enum.Permission.LOCATION)
                    }
                }
            }
        }

    private val gpsReqLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            //check for gps access
            if (viewModel.arePermissionsEnabled(requireContext())) {
                startLocationUpdates()
            }
        }

    private fun showLocationPermissionDialog(permission: Enum.Permission) {

        val title: String
        val desc: String

        when (permission) {
            Enum.Permission.GPS -> {
                title = getString(R.string.gps_permission_title)
                desc = getString(R.string.gps_permission_desc)
            }

            Enum.Permission.LOCATION -> {
                title = getString(R.string.location_permission_title)
                desc = getString(R.string.location_permission_desc)
            }
        }

        PermissionRequestDialog(title = title,
            desc = desc,
            positiveText = getString(R.string.allow),
            negativeText = getString(R.string.decline),
            iconId = R.drawable.ic_location,
            listener = object : PermissionRequestDialogClickListener {
                override fun onCrossClicked() {
                    // GG
                }

                override fun onPositiveClicked() {
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

                override fun onNegativeClicked() {
                    // GG
                }

            }).show(
            childFragmentManager, null
        )

        /*val alertBinding: DialogLocationRequestBinding =
            DialogLocationRequestBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(this, R.style.CustomAlertDialog)
            .create()
        builder.setView(alertBinding.root)

        when (permission) {
            Enum.Permission.GPS -> {
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
//            Toast.makeText(requireContext(), "confirm", Toast.LENGTH_SHORT).show()
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
        builder.window?.attributes = layoutParams*/
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY, Constants.UPDATE_INTERVAL_IN_MILLISECONDS
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
                    viewModel.lat = currLocation.latitude
                    viewModel.lon = currLocation.longitude/*Log.d(
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
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.requestLocationUpdates(
            locationRequest, locationCallback, locHandler.looper
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