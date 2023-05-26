package com.shutterfly.pixabaygallery.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asFlow
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.shutterfly.pixabaygallery.repositories.FavoritesRepository
import com.shutterfly.pixabaygallery.repositories.GalleryRepository
import kotlinx.coroutines.launch

class GalleryViewModel(
    private val repository: GalleryRepository,
    private val favoritesRepository: FavoritesRepository
    ) : ViewModel() {

    private companion object {
        private const val DEFAULT_SEARCH_KEYWORD = "android"
    }

    private val _currentKeyword = MutableLiveData(DEFAULT_SEARCH_KEYWORD)
    private val _favorites = HashSet<Int>()

    init {
        viewModelScope.launch {
            _favorites.addAll(favoritesRepository.getFavorites())
        }
    }

    val imageListObservable = _currentKeyword.switchMap { keyword ->
        repository.searchImages(keyword)
    }.cachedIn(viewModelScope).asFlow()

    fun onSearchButtonClicked(keyword: String) {
        if (keyword.isNotBlank()) {
            _currentKeyword.value = keyword
        }
    }

    fun addRemoveFavorite(id: Int) {
        if (isLiked(id)) _favorites.remove(id) else _favorites.add(id)
        viewModelScope.launch {
            favoritesRepository.saveFavorites(_favorites.toList())
        }
    }
    fun isLiked(id: Int) = _favorites.contains(id)

}

@Suppress("UNCHECKED_CAST")
class GalleryViewModelFactory(
    private val repository: GalleryRepository,
    private val favoritesRepository: FavoritesRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(GalleryViewModel::class.java)) {
            GalleryViewModel(repository, favoritesRepository) as T
        } else {
            throw IllegalArgumentException("ViewModel not found")
        }
    }
}