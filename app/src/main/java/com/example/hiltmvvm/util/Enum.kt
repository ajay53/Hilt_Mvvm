package com.example.hiltmvvm.util

object Enum{
    enum class SortBy(val type: String) {
        BEST_MATCH("best_match"),
        RATING("rating"),
        REVIEW_COUNT("review_count"),
        DISTANCE("distance")
    }

    enum class Permission {
        GPS, LOCATION
    }
}