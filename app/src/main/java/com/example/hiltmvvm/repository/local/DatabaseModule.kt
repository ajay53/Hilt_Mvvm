package com.example.hiltmvvm.repository.local

import android.content.Context
import androidx.room.Room
import com.example.hiltmvvm.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(ViewModelComponent::class)
@Module
class DatabaseModule {

    @Provides
    @ViewModelScoped
    fun provideDatabase(@ApplicationContext context: Context): DatabaseHandler {
        return Room.databaseBuilder(
            context,
            DatabaseHandler::class.java,
            Constants.DB_NAME
        ).build()
    }

    @Provides
    @ViewModelScoped
    fun provideUserDao(databaseHandler: DatabaseHandler): UserDao {
        return databaseHandler.userDao()
    }
}