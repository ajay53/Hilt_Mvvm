package com.example.hiltmvvm.model

data class PostServiceObject(
    val statusCode: Int,
    val message: String,
    val posts: List<Post>
)

data class Post(
    val userId: Long,
    val id: Long,
    val title: String,
    val body: String
)

