package com.example.searchimagecoroutine.api

import retrofit2.http.GET
import retrofit2.http.Query

interface SearchImageService {
    @GET("/v1/search/image")
    suspend fun searchImage(
        @Query("query") query: String,
        @Query("display") display: Int? = 10,
        @Query("start") start: Int? = 1,
        @Query("sort") sort: String? = "sim",
        @Query("filter") filter: String? = "all"
    ): SearchImageApiResponse
}
