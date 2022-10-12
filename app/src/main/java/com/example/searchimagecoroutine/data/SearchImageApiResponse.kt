package com.example.searchimagecoroutine.data

data class SearchImageApiResponse constructor(
    val lastBuildDate: String,
    val total: Int,
    val start: Int,
    val display: Int,
    val items: List<SearchImageApiItem>
)
