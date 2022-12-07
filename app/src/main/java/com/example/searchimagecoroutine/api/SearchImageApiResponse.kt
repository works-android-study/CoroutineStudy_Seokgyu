package com.example.searchimagecoroutine.api

import com.example.searchimagecoroutine.data.SearchImageApiItem

data class SearchImageApiResponse constructor(
    val lastBuildDate: String,
    val total: Int,
    val start: Int,
    val display: Int,
    val items: List<SearchImageApiItem>
)
