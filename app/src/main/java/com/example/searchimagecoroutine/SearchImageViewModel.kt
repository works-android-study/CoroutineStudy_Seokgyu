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
import javax.inject.Inject

@HiltViewModel
class SearchImageViewModel @Inject constructor(
    private val searchImageRepository: SearchImageRepository
): ViewModel() {

    private val _firstImageItem = MutableLiveData<SearchImageApiItem>()
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
    val flow = _searchText.switchMap {
                Pager(
                    PagingConfig(pageSize = 10)
                ) {
                    CustomPagingSource(searchImageRepository, it)
                }.liveData.cachedIn(viewModelScope)
            }.asFlow()

    fun insertSearchItem(searchImageItem: SearchImageApiItem) =
        viewModelScope.launch {
            if (searchImageRepository.insertSearchItem(searchImageItem) > 0) {
                _isSuccessSaveSearchItem.value = true
            }
        }
}
