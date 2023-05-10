package com.example.hiltmvvm.repository.remote

import com.example.hiltmvvm.model.PostServiceObject
import retrofit2.http.GET

interface ApiService {

    @GET("posts")
    suspend fun getPosts(): PostServiceObject
}