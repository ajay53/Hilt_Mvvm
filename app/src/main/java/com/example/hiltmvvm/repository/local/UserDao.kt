package com.example.hiltmvvm.repository.local

import androidx.room.Dao
import androidx.room.Insert
import com.example.hiltmvvm.model.User

@Dao
interface UserDao {

    @Insert
    suspend fun insert(user: User)
}