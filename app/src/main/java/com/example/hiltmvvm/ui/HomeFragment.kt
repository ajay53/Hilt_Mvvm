package com.example.hiltmvvm.ui

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.HandlerThread
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hiltmvvm.adapter.FilterCategoryRecyclerAdapter
import com.example.hiltmvvm.customview.PermissionRequestDialog
import com.example.hiltmvvm.databinding.FragmentHomeBinding
import com.example.hiltmvvm.model.FilterCategoryObject
import com.example.hiltmvvm.util.Constants
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

class HomeFragment : Fragment(), View.OnClickListener,
    FilterCategoryRecyclerAdapter.FilterCategoryInteraction {

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
        binding.tvRestaurants.setOnClickListener(this)

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
                TODO("Not yet implemented")
            }

            binding.tvRestaurants.id -> {
                /*PermissionRequestDialog("", "", "", "", 123, requireActivity()).show(
                    childFragmentManager,
                    null
                )*/
            }
        }
    }

    override fun onFilterClicked(position: Int, item: FilterCategoryObject) {
        TODO("Not yet implemented")
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
}