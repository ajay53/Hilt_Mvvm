package com.example.hiltmvvm.repository

import android.util.Log
import com.example.hiltmvvm.model.User
import com.example.hiltmvvm.repository.local.UserDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository @Inject constructor() {

    @Inject
    lateinit var userDao: UserDao

    suspend fun insertUser(user: User) {
        userDao.insert(user)
        Log.d(TAG, "insertUser: $user")
    }

    companion object {
        private const val TAG = "MainRepository"
    }
}