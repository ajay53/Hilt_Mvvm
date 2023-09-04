package com.example.hiltmvvm.repository.remote

import com.example.hiltmvvm.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class QueryInterceptor:Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalUrl = originalRequest.url()
        val url = originalUrl.newBuilder()
            .addQueryParameter("key", BuildConfig.API_KEY)
            .build()
        val requestBuilder = originalRequest.newBuilder().url(url)
        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}
