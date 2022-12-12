package com.example.searchimagecoroutine.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SearchImageItem(
    @PrimaryKey(autoGenerate = true)
    val itemId: Int,
    val title: String,
    val link: String,
    val thumbnail: String,
    val sizeHeight: String,
    val sizeWidth: String
)
