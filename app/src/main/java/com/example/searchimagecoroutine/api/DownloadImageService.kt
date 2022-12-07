package com.example.searchimagecoroutine.api

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

interface DownloadImageService {

    @GET
    @Streaming
    suspend fun downloadImage(@Url url: String): ResponseBody
}
