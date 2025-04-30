package com.example.mynewsapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynewsapp.R
import com.example.mynewsapp.data.local.entity.BookmarkEntity
import com.example.mynewsapp.data.model.latestnews.Article
import com.example.mynewsapp.data.model.latestnews.Source
import com.example.mynewsapp.domain.domainmodels.ArticleModel
import com.example.mynewsapp.domain.usecases.bookmark.ConvertBookmarkEntityToArticleUseCase
import com.example.mynewsapp.domain.usecases.bookmark.ReadBookmarksUseCase
import com.example.mynewsapp.domain.usecases.detail.DeleteBookmarkUseCase
import com.example.mynewsapp.presentation.uimodels.common.ArticleUiModel
import com.example.mynewsapp.presentation.uistates.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    var readBookmarksUseCase: ReadBookmarksUseCase,
    val deleteBookmarkUseCase: DeleteBookmarkUseCase,
) : ViewModel() {

    private val _bookmarkState = MutableLiveData<UiState<List<ArticleUiModel>>>()
    val bookmarkState: LiveData<UiState<List<ArticleUiModel>>> get() = _bookmarkState


    private var _isProductDeleted = MutableLiveData<UiState<Int>>()
    val isProductDeleted : LiveData<UiState<Int>> get() = _isProductDeleted


    init {
        loadBookmarks()
    }

    fun loadBookmarks() {
        _bookmarkState.value = UiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val bookmarkFlow = readBookmarksUseCase()
            if (bookmarkFlow.isSuccess) {
                bookmarkFlow.getOrNull()?.collect { bookmarks ->
                    withContext(Dispatchers.Main) {
                        val bookmarkNews = convertArticleModelToArticleUiModel(bookmarks)
                        _bookmarkState.value = UiState.Success(bookmarkNews)
                    }
                }
            } else {
                _bookmarkState.value = UiState.Error(R.string.wrong_something)
            }

        }
    }
    fun convertArticleModelToArticleUiModel(newsList: List<ArticleModel>)=
       newsList.map {news ->
           ArticleUiModel(
           urlToImage = news.urlToImage ?: "No Image Url",
           timeDifference = news.timeDifference,
           title = news.title ?: "No title",
           description = news.description ?: "No description",
           author = news.author ?: "No author",
           content = news.content ?: "No content",
           source = Source(news.source?.id ?: "No id", news.source?.name ?: "No name"),
           url = news.url ?: "No url",
           publishedAt = news.publishedAt ?: "No published at"
       )  }


    fun deleteBookmark(url: String) {
        _isProductDeleted.value = UiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val result =
                deleteBookmarkUseCase(
                    url
                )
            withContext(Dispatchers.Main) {
                _isProductDeleted.value = if (result.isSuccess) {
                    UiState.Success(R.string.successfully_removed)
                } else {
                    UiState.Error(R.string.failed_remove_from_bookmark)
                }
            }
        }
    }

}