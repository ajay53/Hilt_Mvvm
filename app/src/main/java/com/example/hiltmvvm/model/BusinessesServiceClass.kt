package com.example.hiltmvvm.model
data class BusinessesServiceClass(
    val businesses: List<Business>,
    val total: Long,
    val region: Region,
    var code: Int = 500,
    var message: String = "unknown"
)
