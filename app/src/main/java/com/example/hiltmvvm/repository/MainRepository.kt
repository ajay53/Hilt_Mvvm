package com.example.hiltmvvm.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.hiltmvvm.model.Post
import com.example.hiltmvvm.model.User
import com.example.hiltmvvm.repository.local.UserDao
import com.example.hiltmvvm.repository.remote.ApiService
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository @Inject constructor() {

    private var job: CompletableJob? = null

    //local
    @Inject
    lateinit var userDao: UserDao

    suspend fun insertUser(user: User) {
        userDao.insert(user)
        Log.d(TAG, "insertUser: $user")
    }

    //remote
    @Inject
    lateinit var apiService: ApiService

    suspend fun getPostById(id: Long): LiveData<Post> {
        job = Job()
        return object : LiveData<Post>() {
            override fun onActive() {
                super.onActive()
                job?.let {
                    CoroutineScope(Dispatchers.IO + it).launch {
                        apiService.getPostById(id)
                    }
                }
            }
        }
    }

    fun cancelJobs() {
        job?.cancel()
    }

    companion object {
        private const val TAG = "MainRepository"
    }
}