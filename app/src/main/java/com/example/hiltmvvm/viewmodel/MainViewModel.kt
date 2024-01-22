package com.example.hiltmvvm.viewmodel

import android.content.Context
import androidx.lifecycle.*
import com.example.hiltmvvm.model.User
import com.example.hiltmvvm.repository.MainRepository
import com.example.hiltmvvm.util.Util
import com.example.hiltmvvm.model.SearchBusiness
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var radius: Int = 0
    var radiusPosition: Int = 0
    var lat:Double = 0.0
    var lon:Double = 0.0
    var activeSort:String = ""

    fun insertUser(user: User) {
        viewModelScope.launch(Dispatchers.Default) {
            repository.insertUser(user)
        }
    }

    private val _postId: MutableLiveData<Long> = MutableLiveData()
//    private val _postId: MutableLiveData<Long> = MutableLiveData(savedStateHandle["postId"])

    val postServiceObject = _postId.switchMap { postId ->
        repository.getPostById(postId ?: 0)
    }

    //_______________________________________

    fun setPostIdToFetch(postId: Long) {
        _postId.value = postId
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

    fun arePermissionsEnabled(context: Context): Boolean {
        return (Util.isGpsEnabled(context) && Util.hasLocationPermission(context))
    }

    fun cancelJobs() {
        //cancel pending operations if need be
        repository.cancelJobs()
    }

    companion object {
        private const val TAG = "MainViewModel"

        /*val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                // Get the Application object from extras
                val application = checkNotNull(extras[APPLICATION_KEY])
                // Create a SavedStateHandle for this ViewModel from extras
                val savedStateHandle = extras.createSavedStateHandle()

                return MyViewModel(
                    (application as MyApplication).myRepository,
                    savedStateHandle
                ) as T
            }
        }*/
    }
}