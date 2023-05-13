package com.example.hiltmvvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.example.hiltmvvm.model.Post
import com.example.hiltmvvm.model.User
import com.example.hiltmvvm.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {

    fun insertUser(user: User) {
        viewModelScope.launch(Dispatchers.Default) {
            repository.insertUser(user)
        }
    }

    private val _postId: MutableLiveData<Long> = MutableLiveData()

    val postServiceObject = _postId.switchMap {
        repository.getPostById(it)
    }

    fun setPostIdToFetch(postId: Long) {
        _postId.value = postId
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