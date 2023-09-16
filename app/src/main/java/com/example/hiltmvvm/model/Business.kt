package com.example.hiltmvvm.model

import com.squareup.moshi.Json

//import com.google.gson.annotations.Json

data class Business(
    val alias: String,
    val categories: List<Category>,
    val coordinates: Center,

    @Json(name = "display_phone")
    val displayPhone: String,

    val distance: Double,
    val id: String,

    @Json(name = "image_url")
    val imageURL: String,

    @Json(name = "is_closed")
    val isClosed: Boolean,

    val location: Location,
    val name: String,
    val phone: String,
    val price: String,
    val rating: Double,

    @Json(name = "review_count")
    val reviewCount: Long,

    val transactions: List<String>,
    val url: String
)

data class Category(
    val alias: String,
    val title: String
)

data class Center(
    val latitude: Double,
    val longitude: Double
)

data class Location(
    val address1: String,
    val address2: String,
    val address3: String,
    val city: String,
    val country: String,

    @Json(name = "display_address")
    val displayAddress: List<String>,

    val state: String,

    @Json(name = "zip_code")
    val zipCode: String
)

data class Region(
    val center: Center
)
