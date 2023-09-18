package com.example.hiltmvvm.repository

import androidx.lifecycle.LiveData
import com.example.hiltmvvm.model.BusinessesServiceClass
import com.example.hiltmvvm.repository.remote.YelpApiService
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@ViewModelScoped
class HomeRepository @Inject constructor() {

    private var job: CompletableJob? = null

    //remote
    @Inject
    lateinit var yelpApiService: YelpApiService

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
                        val resBody = yelpApiService.searchBusinessesBody(
                            lat,
                            lon,
                            radius,
                            sortBy,
                            limit,
                            offset
                        )

                        val body = resBody.body().apply {
                            this?.code = resBody.code()
                            this?.message = resBody.message()
                        }

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
}