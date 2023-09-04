package com.example.hiltmvvm.repository.remote

import com.example.hiltmvvm.model.Post
import com.example.hiltmvvm.model.PostServiceObject
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("posts")
    suspend fun getPosts(): PostServiceObject

    @GET("posts/{Id}")
//    suspend fun getPostById(@Path("Id") id: Long): Post
    suspend fun getPostById(@Path("Id") id: Long): Response<Post>

    suspend fun getImages(@Path("Id") id: Long): Response<Post>
}