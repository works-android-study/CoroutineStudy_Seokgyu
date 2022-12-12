package com.example.searchimagecoroutine

import com.example.searchimagecoroutine.api.SearchImageService
import com.example.searchimagecoroutine.data.SearchImageApiItem
import com.example.searchimagecoroutine.api.SearchImageApiResponse
import com.example.searchimagecoroutine.data.toSearchImageApiItem
import com.example.searchimagecoroutine.data.toSearchImageItem
import com.example.searchimagecoroutine.room.AppDatabase
import com.example.searchimagecoroutine.room.entity.SearchImageItem
import javax.inject.Inject

class SearchImageRepository @Inject constructor(
    private val searchImageService: SearchImageService,
    private val searchImageDatabase: AppDatabase

) {

    suspend fun fetchSearchImage(query: String, display: Int, start: Int): SearchImageApiResponse {
        return searchImageService.searchImage(query, display = display, start = start)
    }

    suspend fun insertSearchItem(searchImageApiItem: SearchImageApiItem): Long {
        return searchImageDatabase.searchImageDao().insert(searchImageApiItem.toSearchImageItem())
    }

    suspend fun getAllSearchItem(): List<SearchImageApiItem> {
        return searchImageDatabase.searchImageDao().getAll().map {
            it.toSearchImageApiItem()
        }
    }

    suspend fun getBookmarkItems(start: Int, size: Int): List<SearchImageItem> {
        return searchImageDatabase.searchImageDao().getBookmarkItems(size, start * size)
    }
}
