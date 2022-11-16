package com.example.searchimagecoroutine.data

import com.example.searchimagecoroutine.room.entity.SearchImageItem

data class SearchImageApiItem constructor(
    val title: String,
    val link: String,
    val thumbnail: String,
    val sizeheight: String,
    val sizewidth: String
)


fun SearchImageApiItem.toSearchImageItem(): SearchImageItem {
    return SearchImageItem(
        0,
        this.title,
        this.link,
        this.sizeheight,
        this.sizewidth
    )
}

fun SearchImageItem.toSearchImageApiItem(): SearchImageApiItem {
    return SearchImageApiItem(
        title = this.title,
        link = this.link,
        thumbnail = "",
        sizeheight = this.sizeHeight,
        sizewidth = this.sizeWidth
    )
}
