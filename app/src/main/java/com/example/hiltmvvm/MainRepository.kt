package com.example.hiltmvvm

import javax.inject.Inject

class MainRepository @Inject constructor() {

    suspend fun insertUser(user: User) {
        println("User: $user")
    }
}