package com.example.hiltmvvm.repository.local

import android.content.Context
import androidx.room.Room
import com.example.hiltmvvm.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): DatabaseHandler {
        return Room.databaseBuilder(
            context,
            DatabaseHandler::class.java,
            Constants.DB_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideUserDao(databaseHandler: DatabaseHandler): UserDao {
        return databaseHandler.userDao()
    }
}