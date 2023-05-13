package com.example.hiltmvvm.model

data class AllPostServiceObject(
    val statusCode: Int,
    val message: String,
    val posts: List<Post>
)

data class PostServiceObject(
    var statusCode: Int,
    var message: String,
    val post: Post?
)

data class Post(
    val userId: Long,
    val id: Long,
    val title: String,
    val body: String
)

