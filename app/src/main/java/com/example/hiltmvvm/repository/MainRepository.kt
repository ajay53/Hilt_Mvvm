package com.example.hiltmvvm.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.hiltmvvm.model.BusinessesServiceClass
import com.example.hiltmvvm.model.Post
import com.example.hiltmvvm.model.PostServiceObject
import com.example.hiltmvvm.model.User
import com.example.hiltmvvm.repository.local.UserDao
import com.example.hiltmvvm.repository.remote.ApiService
import com.example.hiltmvvm.repository.remote.YelpApiService
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

    @Inject
    lateinit var yelpApiService: YelpApiService

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

    //________________________________

    fun searchBusinesses(
        lat: Double,
        lon: Double,
        radius: Int,
        sortBy: String,
        limit: Int,
        offset: Int
    ): LiveData<BusinessesServiceClass> {
        job = Job()
        return object : LiveData<BusinessesServiceClass>() {
            override fun onActive() {
                super.onActive()
                job?.let {
                    CoroutineScope(Dispatchers.IO + it).launch {
                        /*val business: BusinessesServiceClass =
                            RetrofitBuilder.apiService.searchBusinesses(
                                lat,
                                lon,
                                radius,
                                sortBy,
                                limit,
                                offset
                            )*/

                        val resBody = yelpApiService.searchBusinessesBody(
                            lat,
                            lon,
                            radius,
                            sortBy,
                            limit,
                            offset
                        )
                        val body = resBody.body()?.apply {
                            this.code = resBody.code()
                            this.message = resBody.message()
                        }

                        /*withContext(Main){
                            value = business
                            it.complete()
                        }*/
//                        postValue(resBody.body())
                        postValue(body)
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