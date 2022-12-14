package com.example.searchimagecoroutine

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.searchimagecoroutine.api.SearchImageService
import com.example.searchimagecoroutine.data.SearchImageApiItem
import com.example.searchimagecoroutine.data.toSearchImageApiItem
import com.example.searchimagecoroutine.room.AppDatabase
import com.example.searchimagecoroutine.room.dao.SearchImageDao
import com.example.searchimagecoroutine.support.CoroutineDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.bouncycastle.util.test.SimpleTest.runTest
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@ExperimentalCoroutinesApi
@RunWith(
    RobolectricTestRunner::class)
@Config(sdk = [32])
class SearchImageRoomDatabaseTest {
    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    val testDispatcherRule = CoroutineDispatcherRule()


    lateinit var db: AppDatabase
    lateinit var searchImageDao: SearchImageDao

    lateinit var searchImageRepository: SearchImageRepository
    lateinit var searchImageService: SearchImageService
    @Before
    fun setup() {
        createDb()
        searchImageService = Mockito.mock(SearchImageService::class.java)
        searchImageRepository = SearchImageRepository(
            searchImageService,
            db
        )
    }

    @Test
    fun insert_getAll_test(): Unit = runTest {
        //given
        val data1 = makeDefaultSearchImageApiItem()
        val data2 = makeDefaultSearchImageApiItem()
        val data3 = makeDefaultSearchImageApiItem()


        //when
        searchImageRepository.insertSearchItem(data1)
        searchImageRepository.insertSearchItem(data2)
        searchImageRepository.insertSearchItem(data3)

        //then
        val fetchedList = searchImageRepository.getAllSearchItem()

        assertEquals(data1, fetchedList[0])
        assertEquals(data2, fetchedList[1])
        assertEquals(data3, fetchedList[2])
    }

    @Test
    fun getBookmarkItems_test(): Unit = runTest {
        //given
        val data1 = makeDefaultSearchImageApiItem()
        val data2 = makeDefaultSearchImageApiItem()
        val data3 = makeDefaultSearchImageApiItem()


        //when
        searchImageRepository.insertSearchItem(data1)
        searchImageRepository.insertSearchItem(data2)
        searchImageRepository.insertSearchItem(data3)

        //then
        val fetchedList = searchImageRepository.getBookmarkItems(0, 20)

        assertEquals(data3, fetchedList[0].toSearchImageApiItem())
        assertEquals(data2, fetchedList[1].toSearchImageApiItem())
        assertEquals(data1, fetchedList[2].toSearchImageApiItem())

    }

    private fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).allowMainThreadQueries()
            .build()
        searchImageDao = db.searchImageDao()
    }

    private fun makeDefaultSearchImageApiItem(): SearchImageApiItem {
        return SearchImageApiItem(
            link = "https://i3.ruliweb.com/img/22/10/07/183aff041c248db7e.jpg",
            sizeheight = "2000",
            sizewidth = "900",
            thumbnail = "https://search.pstatic.net/sunny/?src=https://i3.ruliweb.com/img/22/10/07/183aff041c248db7e.jpg&type=b150",
            title = "주식) 카카오 4형제 근황 | 유머 게시판 주식) 카카오 4형제 근황 | 유머 게시판 | 루리웹"
        )
    }
}
