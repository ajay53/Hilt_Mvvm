package com.example.hiltmvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.example.hiltmvvm.repository.HomeRepository
import com.goazzi.skycore.model.SearchBusiness
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: HomeRepository) : ViewModel() {

    private val _searchBusiness: MutableLiveData<SearchBusiness> = MutableLiveData()

    val businessServiceClass = _searchBusiness.switchMap {
        repository.searchBusinesses(it.lat, it.lon, it.radius, it.sortBy, it.limit, it.offset)
    }

    fun cancelJobs() {
        //cancel pending operations if need be
        repository.cancelJobs()
    }
}