package com.example.hiltmvvm

import android.util.Log
import javax.inject.Inject

class MainRepository @Inject constructor() {

    suspend fun insertUser(user: User) {
        Log.d(TAG, "insertUser: $user")
    }

    companion object {
        private const val TAG = "MainRepository"
    }
}