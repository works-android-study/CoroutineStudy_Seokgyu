package com.example.searchimagecoroutine

import android.content.Context
import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.searchimagecoroutine.api.SearchImageApiResponse
import com.example.searchimagecoroutine.data.SearchImageApiItem
import com.example.searchimagecoroutine.room.AppDatabase
import com.example.searchimagecoroutine.room.dao.SearchImageDao
import com.example.searchimagecoroutine.support.CoroutineDispatcherRule
import com.example.searchimagecoroutine.support.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.*
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import com.example.searchimagecoroutine.*
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.toList
import org.junit.Assert.assertEquals
import javax.inject.Inject

@ExperimentalCoroutinesApi


class SearchImageViewModelTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    val testDispatcherRule = CoroutineDispatcherRule()


    lateinit var searchImageRepository: SearchImageRepository

    lateinit var searchImageViewModel: SearchImageViewModel

    @Before
    fun setUp(): Unit = runTest {
        searchImageRepository = Mockito.mock(SearchImageRepository::class.java)
        mockSearchImageRepository()
        searchImageViewModel = SearchImageViewModel(
            searchImageRepository,
            UnconfinedTestDispatcher()
        )
    }




    @Test
    fun when_db_insert_success_isSuccessSaveSearchItem_update_test(): Unit = runTest {
        //given
        val data = makeDefaultSearchImageApiItem()

        //when
        searchImageViewModel.insertSearchItem(data)

        //then
        assertEquals(searchImageViewModel.isSuccessSaveSearchItem.getOrAwaitValue(), true)
    }

    @Test
    fun set_currentSateItem_test(): Unit = runTest {
        //given
        val data = makeDefaultSearchImageApiItem()

        //when
        searchImageViewModel.setCurrentStateItem(data)

        //then
        assertEquals(searchImageViewModel.currentStateItem.value, data)

    }

    @Test
    fun downloarRateFlow_emit_test(): Unit = runTest {
        //given, when
        val emittedList = emit()

        val flowList =  searchImageViewModel.downloadRateFlow.value

        //then
        assertEquals(emittedList.last(), flowList)
    }




    private suspend fun mockSearchImageRepository() {
        Mockito.`when`(searchImageRepository.insertSearchItem(com.example.searchimagecoroutine.support.any())).thenReturn(1)
        Mockito.`when`(searchImageRepository.fetchSearchImage(
            anyString(), anyInt(), anyInt()
        )).thenReturn(
            SearchImageApiResponse(
                lastBuildDate = "Thu, 15 Dec 2022 00:13:16 +0900",
                start = 1,
                total = 4534157,
                display = 20,
                items = listOf(
                    SearchImageApiItem(
                        link = "https://i3.ruliweb.com/img/22/10/07/183aff041c248db7e.jpg",
                        sizeheight = "2000",
                        sizewidth = "900",
                        thumbnail = "https://search.pstatic.net/sunny/?src=https://i3.ruliweb.com/img/22/10/07/183aff041c248db7e.jpg&type=b150",
                        title = "주식) 카카오 4형제 근황 | 유머 게시판 주식) 카카오 4형제 근황 | 유머 게시판 | 루리웹"
                    )
                )
            )
        )
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


    private suspend fun emit(): MutableList<Int> {
        val list = mutableListOf<Int>()
        for (i in 0 until 10) {
            searchImageViewModel.emit(i)
            list.add(i)
        }
        return list
    }

}
