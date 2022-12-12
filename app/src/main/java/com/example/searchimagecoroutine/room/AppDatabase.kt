package com.example.searchimagecoroutine.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.searchimagecoroutine.room.dao.SearchImageDao
import com.example.searchimagecoroutine.room.entity.SearchImageItem

@Database(entities = [SearchImageItem::class], version = 2)
abstract class AppDatabase: RoomDatabase() {
    abstract fun searchImageDao(): SearchImageDao
}
