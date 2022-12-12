package com.example.searchimagecoroutine

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.searchimagecoroutine.data.SearchImageApiItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import androidx.paging.liveData
import com.example.searchimagecoroutine.data.CustomPagingSource
import com.example.searchimagecoroutine.hilt.RetrofitModule
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import okhttp3.ResponseBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class SearchImageViewModel @Inject constructor(
    private val searchImageRepository: SearchImageRepository,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val imageDownloadClient by lazy {
        RetrofitModule.createDownloadImageClient {
            viewModelScope.launch {
                _downloadRateFLow.emit(it)
                if (it == 100) {
                    _isSuccessDownloadItem.postValue(true)
                    _downloadRateFLow.emit(-1)
                }
            }
        }
    }
    //Todo ApiItem을 사용하는 부분 리팩토링, DTO 정리
    private val _currentStateItem = MutableLiveData<SearchImageApiItem>()
    val currentStateItem: LiveData<SearchImageApiItem>
        get() = _currentStateItem

    private val _downloadRateFLow = MutableStateFlow<Int>(-1)
    val downloadRateFlow: StateFlow<Int>
        get() = _downloadRateFLow

    private val _isSuccessDownloadItem = MutableLiveData<Boolean>(false)
    val isSuccessDownloadItem: LiveData<Boolean>
        get() = _isSuccessDownloadItem

    private val _isSuccessSaveSearchItem = MutableLiveData<Boolean>(false)
    val isSuccessSaveSearchItem: LiveData<Boolean>
        get() = _isSuccessSaveSearchItem

    var isSearchMode: Boolean = false
    val inputText = mutableStateOf(TextFieldValue(""))

    private val _searchText = MutableLiveData<String>("")
    val searchText: LiveData<String>
        get() = _searchText

    fun setSearchText(searchText: String) {
        _searchText.value = searchText
    }

    fun setCurrentStateItem(searchImageApiItem: SearchImageApiItem) {
        _currentStateItem.value = searchImageApiItem
    }

    val flow = _searchText.switchMap {
        Pager(
            PagingConfig(pageSize = CustomPagingSource.DISPLAY)
        ) {
            CustomPagingSource(searchImageRepository, it)
        }.liveData.cachedIn(viewModelScope)
    }.asFlow()

    fun insertSearchItem(searchImageItem: SearchImageApiItem) =
        viewModelScope.launch(ioDispatcher) {
            if (searchImageRepository.insertSearchItem(searchImageItem) > 0) {
                _isSuccessSaveSearchItem.postValue(true)
            }
        }

    fun imageDownload(imageUrl: String, filePath: String) = viewModelScope.launch(ioDispatcher) {
        imageDownloadClient.downloadImage(imageUrl)
            .use { body ->
                val fileName = imageUrl.substringAfterLast("/")

                saveFile(body, "$filePath/$fileName")
            }

    }

    private fun saveFile(body: ResponseBody, filePath: String) {
        val saveFile = File(filePath)
        saveFile.outputStream().use { fileOutput ->
            body.byteStream().copyTo(fileOutput)
        }
    }

}
