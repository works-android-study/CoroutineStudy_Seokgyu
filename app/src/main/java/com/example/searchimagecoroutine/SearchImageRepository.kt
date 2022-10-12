package com.example.searchimagecoroutine

import com.example.searchimagecoroutine.data.SearchImageApiResponse
import javax.inject.Inject

class SearchImageRepository @Inject constructor(
    private val searchImageService: SearchImageService
) {

    suspend fun fetchSearchImage(query: String): SearchImageApiResponse {
        return searchImageService.searchImage(query)
    }
}
