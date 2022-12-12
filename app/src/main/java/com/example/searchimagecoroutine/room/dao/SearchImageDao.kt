package com.example.searchimagecoroutine.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.searchimagecoroutine.room.entity.SearchImageItem

@Dao
interface SearchImageDao {
    @Query("SELECT * FROM searchimageitem")
    suspend fun getAll(): List<SearchImageItem>

    @Query("SELECT * FROM searchimageitem ORDER BY itemId DESC LIMIT :limit OFFSET :offset")
    suspend fun getBookmarkItems(limit: Int, offset: Int): List<SearchImageItem>

    @Insert
    suspend fun insert(item: SearchImageItem): Long
}
