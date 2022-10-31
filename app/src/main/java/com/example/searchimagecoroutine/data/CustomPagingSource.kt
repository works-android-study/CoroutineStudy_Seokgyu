package com.example.searchimagecoroutine.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.searchimagecoroutine.SearchImageRepository

class CustomPagingSource(
    private val searchImageRepository: SearchImageRepository,
    private val query: String
): PagingSource<Int, SearchImageApiItem>() {
    override fun getRefreshKey(state: PagingState<Int, SearchImageApiItem>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchImageApiItem> {
        val nextPageNum = params.key ?: 0
        val response = searchImageRepository.fetchSearchImage(query, display = DISPLAY, start = (nextPageNum * DISPLAY) + 1)

        return LoadResult.Page(
            data = response.items,
            prevKey = null,
            nextKey = nextPageNum + 1
        )
    }

    companion object {
        const val DISPLAY = 20
    }
}
