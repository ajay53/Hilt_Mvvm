package com.example.hiltmvvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.example.hiltmvvm.repository.HomeRepository
import com.goazzi.skycore.model.SearchBusiness
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: HomeRepository) : ViewModel() {

    private var _isLocationSwitchEnabled: MutableLiveData<Boolean> = MutableLiveData(false)
    val isLocationSwitchEnabled: LiveData<Boolean>
        get() = _isLocationSwitchEnabled

    fun setLocationSwitch(isEnabled: Boolean) {
        _isLocationSwitchEnabled.value = isEnabled
        _radius.value = 100f
    }

    private var _radius: MutableLiveData<Float> = MutableLiveData(100f)
    val radius: LiveData<Float>
        get() = _radius

    fun setRadius(radius: Float) {
        _radius.value = radius
    }

    //seekbar
    private var _seekbarProgress: MutableLiveData<Int> = MutableLiveData(0)
    val seekbarProgress: LiveData<Int>
        get() = _seekbarProgress
    fun setSeekbarProgress(progress: Int) {
        _seekbarProgress.value = progress
    }

    private val _searchBusiness: MutableLiveData<SearchBusiness> = MutableLiveData()

    val businessServiceClass = _searchBusiness.switchMap {
        repository.searchBusinesses(it.lat, it.lon, it.radius, it.sortBy, it.limit, it.offset)
    }

    fun setSearchBusiness(searchBusiness: SearchBusiness) {
        _searchBusiness.value = searchBusiness
        /*if (searchBusiness != _searchBusiness.value) {
            _searchBusiness.value = searchBusiness
        }*/
    }

    fun cancelJobs() {
        //cancel pending operations if need be
        repository.cancelJobs()
    }
}