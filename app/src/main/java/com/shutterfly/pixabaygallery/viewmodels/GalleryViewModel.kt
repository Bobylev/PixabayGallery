package com.shutterfly.pixabaygallery.viewmodels

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.shutterfly.pixabaygallery.repositories.GalleryRepository

class GalleryViewModel(private val repository: GalleryRepository) : ViewModel() {

    private companion object {
        private const val DEFAULT_SEARCH_KEYWORD = "android"
    }

    private val _currentKeyword = MutableLiveData(DEFAULT_SEARCH_KEYWORD)

    val imageListObservable = _currentKeyword.switchMap { keyword ->
        repository.searchImages(keyword)
    }.cachedIn(viewModelScope).asFlow()

    fun onSearchButtonClicked(keyword: String) {
        if (keyword.isNotBlank()) {
            _currentKeyword.value = keyword
        }
    }
}

class GalleryViewModelFactory(private val repository: GalleryRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(GalleryViewModel::class.java)) {
            GalleryViewModel(repository) as T
        } else {
            throw IllegalArgumentException("ViewModel not found")
        }
    }
}