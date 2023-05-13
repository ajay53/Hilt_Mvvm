package com.example.hiltmvvm.repository.remote

import com.example.hiltmvvm.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RetrofitModule {

    /*@Provides
    fun providesRetrofitBuilder(): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(mOkHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
    }*/

    @Singleton
    @Provides
    fun providesApiService(): ApiService {
        val mOkHttpClient: OkHttpClient by lazy {
            OkHttpClient.Builder()
                .readTimeout(3, TimeUnit.SECONDS)
                .connectTimeout(3, TimeUnit.SECONDS)
                .build()
        }

        val retrofitBuilder: Retrofit.Builder by lazy {
            Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(mOkHttpClient)
                .addConverterFactory(MoshiConverterFactory.create())
        }

        return retrofitBuilder
            .build()
            .create(ApiService::class.java)
    }
}