package com.example.hiltmvvm.model

data class SearchBusiness(
    val lat: Double,
    val lon: Double,
    val radius: Int,
    val sortBy: String,
    val limit: Int,
    val offset:Int
)
