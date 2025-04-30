package com.example.mynewsapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynewsapp.R
import com.example.mynewsapp.data.model.latestnews.Article
import com.example.mynewsapp.data.model.latestnews.Source
import com.example.mynewsapp.domain.domainmodels.ArticleModel
import com.example.mynewsapp.domain.usecases.search.GetSearchedNewsUseCase
import com.example.mynewsapp.presentation.mappers.toUi
import com.example.mynewsapp.presentation.uimodels.common.ArticleUiModel
import com.example.mynewsapp.presentation.uistates.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
  val getSearchedNewsUseCase: GetSearchedNewsUseCase
) : ViewModel() {
    private val _searchedNews = MutableLiveData<UiState<ArrayList<ArticleUiModel>>>()
    val searchedNews: LiveData<UiState<ArrayList<ArticleUiModel>>> = _searchedNews




    private var currentPage = 1
    private var currentPageSize = 10
    private var currentQuery: String = ""
    private var totalResult = 100
    private var newsList = ArrayList<ArticleUiModel>()
    private val sortByPublishedAt = "publishedAt"



    fun searchNews(query: String){
        if(query != currentQuery){
            currentPage = 1
            currentQuery= query
            newsList.clear()
        }
        fetchNews()
    }

    fun loadMoreNews(){
        if(newsList.size < totalResult){
            currentPage ++
            fetchNews()
        }

    }
    fun fetchNews() {
        _searchedNews.value = UiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val result =
                getSearchedNewsUseCase(
                 sortBy =  sortByPublishedAt, pageSize =  currentPageSize, query =  currentQuery, page =  currentPage,
                )
            withContext(Dispatchers.Main) {
                if(result.isSuccess){
                    if(currentPage == 1){
                        newsList.clear()
                    }
                    val data = result.getOrNull() ?: emptyList()
                    val news = data.map { it.toUi() }
                    newsList.addAll(news)
                    _searchedNews.value = UiState.Success(newsList)
                }
                else{
                    _searchedNews.value = UiState.Error(R.string.wrong_something)
                }

            }
        }
    }





}