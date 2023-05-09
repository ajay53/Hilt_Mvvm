package com.example.hiltmvvm

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class DatabaseHandler : RoomDatabase() {
    abstract fun userDao(): UserDao
}