package com.example.hiltmvvm

import android.util.Log
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class MainRepository @Inject constructor(private val databaseHandler: DatabaseHandler) {

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