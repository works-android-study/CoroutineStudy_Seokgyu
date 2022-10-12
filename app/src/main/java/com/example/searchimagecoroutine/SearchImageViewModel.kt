package com.example.searchimagecoroutine

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.searchimagecoroutine.data.SearchImageApiItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchImageViewModel @Inject constructor(
    private val searchImageRepository: SearchImageRepository
): ViewModel() {
    private val _firstImageItem = MutableLiveData<SearchImageApiItem>()

    val firstImageItem: LiveData<SearchImageApiItem>
        get() = _firstImageItem

    fun fetchSearchImage(query: String) = viewModelScope.launch {
        val response = searchImageRepository.fetchSearchImage(query)

        if (response.items.isNotEmpty()) {
            _firstImageItem.value = response.items[0]
        }
    }

}
