package com.example.hiltmvvm.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.hiltmvvm.model.Post
import com.example.hiltmvvm.model.PostServiceObject
import com.example.hiltmvvm.model.User
import com.example.hiltmvvm.repository.local.UserDao
import com.example.hiltmvvm.repository.remote.ApiService
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.*
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@ViewModelScoped
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

    fun getPostById(id: Long): LiveData<PostServiceObject> {
        job = Job()
        return object : LiveData<PostServiceObject>() {
            override fun onActive() {
                super.onActive()
                job?.let {
                    CoroutineScope(Dispatchers.IO + it).launch {
                        val response: Response<Post> = apiService.getPostById(id)
                        val postServiceObject = PostServiceObject(response.code(), response.message(), response.body())
                        postValue(postServiceObject)
                        it.complete()
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