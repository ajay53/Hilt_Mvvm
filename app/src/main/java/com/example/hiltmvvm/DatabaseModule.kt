package com.example.hiltmvvm

import android.content.Context
import androidx.room.Room
import com.example.hiltmvvm.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped

@InstallIn(ActivityComponent::class)
@Module
class DatabaseModule {

    @Provides
    @ActivityScoped
    fun provideDatabase(@ActivityContext context: Context): DatabaseHandler {
        return Room.databaseBuilder(
            context,
            DatabaseHandler::class.java,
            Constants.DB_NAME
        ).build()
    }

    @Provides
    @ActivityScoped
    fun provideUserDao(databaseHandler: DatabaseHandler): UserDao {
        return databaseHandler.userDao()
    }
}