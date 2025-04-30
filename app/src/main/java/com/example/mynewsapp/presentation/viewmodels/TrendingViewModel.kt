package com.example.mynewsapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynewsapp.R
import com.example.mynewsapp.data.model.latestnews.Article
import com.example.mynewsapp.data.model.latestnews.Source
import com.example.mynewsapp.domain.domainmodels.ArticleModel
import com.example.mynewsapp.domain.usecases.commonusecases.GetProcessedNewsUseCase
import com.example.mynewsapp.presentation.uimodels.common.ArticleUiModel
import com.example.mynewsapp.presentation.uistates.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TrendingViewModel @Inject constructor(val getProcessedNewsUseCase: GetProcessedNewsUseCase) :
    ViewModel() {

    private var _trendingNews = MutableLiveData<UiState<List<ArticleUiModel>>>(UiState.Loading)
    val trendingNews: LiveData<UiState<List<ArticleUiModel>>> get() = _trendingNews

    private val sortByPopularity = "popularity"
    private val keyWord = "everything"

    init {
        getTrendingNews()
    }

    fun getTrendingNews() {
        this._trendingNews.value = UiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val trendingNews = getProcessedNewsUseCase(keyWord,sortByPopularity)
            withContext(Dispatchers.Main) {
                if(trendingNews.isSuccess){
                  val data =  trendingNews.getOrNull() ?: emptyList()
                    val list = convertArticleModelToArticleUiModel(data)
                    _trendingNews.value = UiState.Success(list)
                }
                else{
                      _trendingNews.value = UiState.Error(R.string.wrong_something)
                }

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

}