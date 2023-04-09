package com.example.hiltmvvm

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_user")
data class User(
    @PrimaryKey @ColumnInfo(name = "user_id") val userId: Long,
    val username: String,
    var password: String
)
