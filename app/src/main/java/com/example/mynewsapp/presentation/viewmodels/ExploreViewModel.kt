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
import com.example.mynewsapp.domain.usecases.exploreusecases.GetSavedTopicsUseCase
import com.example.mynewsapp.domain.usecases.exploreusecases.GetTopicsUseCases
import com.example.mynewsapp.domain.usecases.exploreusecases.SaveTopicUseCase
import com.example.mynewsapp.domain.usecases.exploreusecases.UnSaveTopicUseCase
import com.example.mynewsapp.presentation.mappers.toUi
import com.example.mynewsapp.presentation.uimodels.common.ArticleUiModel
import com.example.mynewsapp.presentation.uimodels.common.TopicUiModel
import com.example.mynewsapp.presentation.uistates.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    val getProcessedNewsUseCase: GetProcessedNewsUseCase,
    val getTopicsUseCases: GetTopicsUseCases,
    val getSavedTopicsUseCase: GetSavedTopicsUseCase,
    val saveTopicUseCase: SaveTopicUseCase,
    val unSaveTopicUseCase: UnSaveTopicUseCase,
) : ViewModel() {
    private var _trendingNews = MutableLiveData<UiState<List<ArticleUiModel>>>(UiState.Loading)
    val trendingNews: LiveData<UiState<List<ArticleUiModel>>> get() = _trendingNews

    private var _topics = MutableLiveData<UiState<ArrayList<TopicUiModel>>>()
    val topics: LiveData<UiState<ArrayList<TopicUiModel>>> get() = _topics

    private var _seeAllStatus = MutableLiveData<Boolean>(false)
    val seeAllStatus: LiveData<Boolean> get() = _seeAllStatus

    var topicList = listOf<TopicUiModel>()
    var topicCount: Int = 3

    init {
        getTopics()
        getTrendingNews()
    }

    fun getTrendingNews() {
        _trendingNews.value = UiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val trendingNews = getProcessedNewsUseCase("everything", "popularity")
            withContext(Dispatchers.Main) {
                if (trendingNews.isSuccess) {
                    val data = trendingNews.getOrNull() ?: emptyList()
                    val news = convertArticleModelToArticleUiModel(data)
                    _trendingNews.value = UiState.Success(news)
                } else {
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

    fun saveTopics(topicName: String) {
        _topics.value = UiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val result = saveTopicUseCase(topicName)
            withContext(Dispatchers.Main) {
                if (result.isFailure) {
                    _topics.value = UiState.Error(R.string.failed_to_save_topics)
                }
            }
        }
    }

    fun unSaveTopics(topicName: String) {
        _topics.value = UiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val result = unSaveTopicUseCase(topicName)
            withContext(Dispatchers.Main) {
                if (result.isFailure) {
                    _topics.value = UiState.Error(R.string.failed_to_unsave_topics)
                }
            }
        }
    }

    fun getTopics() {
        _topics.value = UiState.Loading
        val topics = getTopicsUseCases()

        viewModelScope.launch(Dispatchers.IO) {
            getSavedTopicsUseCase().collect { savedTopicsResult ->
                if (savedTopicsResult.isSuccess) {
                    val savedTopics = savedTopicsResult.getOrNull() ?: emptyList()

                    topicList = topics.map { topic ->
                        topic.toUi(savedTopics.contains(topic.topic))
                    }
                    withContext(Dispatchers.Main) {
                        _topics.value = UiState.Success(ArrayList(topicList.take(topicCount)))
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        _topics.value = UiState.Error(R.string.failed_to_topics)
                    }
                }
            }
        }
    }


    fun onSaveClick(topicUiModel: TopicUiModel) {
        if (topicUiModel.isSaved) {
            unSaveTopics(topicUiModel.topic)
        } else {
            saveTopics(topicUiModel.topic)
        }

    }

    fun toggleSeeAll() {
        if (seeAllStatus.value!!) topicCount = 3
        else topicCount = 5
        _seeAllStatus.value = !seeAllStatus.value!!
        _topics.value = UiState.Success(ArrayList(topicList.take(topicCount)))

    }
}