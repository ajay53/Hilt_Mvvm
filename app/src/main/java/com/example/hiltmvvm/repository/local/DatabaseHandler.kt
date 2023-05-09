package com.example.hiltmvvm.repository.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.hiltmvvm.model.User

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class DatabaseHandler : RoomDatabase() {
    abstract fun userDao(): UserDao
}