package com.example.mynewsapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynewsapp.R
import com.example.mynewsapp.data.model.latestnews.Article
import com.example.mynewsapp.domain.usecases.commonusecases.GetProcessedNewsUseCase
import com.example.mynewsapp.domain.usecases.exploreusecases.GetSavedTopicsUseCase
import com.example.mynewsapp.domain.usecases.exploreusecases.GetTopicsUseCases
import com.example.mynewsapp.domain.usecases.exploreusecases.SaveTopicUseCase
import com.example.mynewsapp.domain.usecases.exploreusecases.UnSaveTopicUseCase
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
    private var _trendingNews = MutableLiveData<UiState<List<Article>>>(UiState.Loading)
    val trendingNews: LiveData<UiState<List<Article>>> get() = _trendingNews

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
                    _trendingNews.value = UiState.Success(data)
                } else {
                    _trendingNews.value = UiState.Error(R.string.wrong_something)
                }

            }
        }
    }

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
                        TopicUiModel(
                            topic.topicImg,
                            topic.topic,
                            topic.topicDescription,
                            savedTopics.contains(topic.topic)
                        )
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