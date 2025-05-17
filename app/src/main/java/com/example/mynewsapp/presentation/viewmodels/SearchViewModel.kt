package com.example.mynewsapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynewsapp.R
import com.example.mynewsapp.domain.domainmodels.AuthorModel
import com.example.mynewsapp.domain.domainmodels.FollowModel
import com.example.mynewsapp.domain.domainmodels.TopicModel
import com.example.mynewsapp.domain.usecases.commonusecases.GetFollowedSourcesUseCase
import com.example.mynewsapp.domain.usecases.detailusecases.FollowNewsSourceUseCase
import com.example.mynewsapp.domain.usecases.detailusecases.IsNewsSourceFollowedUseCase
import com.example.mynewsapp.domain.usecases.detailusecases.UnfollowNewSourceUseCase
import com.example.mynewsapp.domain.usecases.exploreusecases.GetSavedTopicsUseCase
import com.example.mynewsapp.domain.usecases.exploreusecases.GetTopicsUseCases
import com.example.mynewsapp.domain.usecases.exploreusecases.SaveTopicUseCase
import com.example.mynewsapp.domain.usecases.exploreusecases.UnSaveTopicUseCase
import com.example.mynewsapp.domain.usecases.search.GetSearchedNewsUseCase
import com.example.mynewsapp.domain.usecases.searchingusecases.GetAuthorsUseCase
import com.example.mynewsapp.domain.usecases.searchingusecases.GetSearchCategoriesUseCase
import com.example.mynewsapp.domain.usecases.searchingusecases.SearchAuthorsUseCase
import com.example.mynewsapp.domain.usecases.searchingusecases.SearchTopicsUseCase
import com.example.mynewsapp.presentation.enums.SearchCategory
import com.example.mynewsapp.presentation.mappers.toUi
import com.example.mynewsapp.presentation.uimodels.author.AuthorUiModel
import com.example.mynewsapp.presentation.uimodels.common.ArticleUiModel
import com.example.mynewsapp.presentation.uimodels.common.TopicUiModel
import com.example.mynewsapp.presentation.uistates.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getSearchedNewsUseCase: GetSearchedNewsUseCase,
    private val getSearchCategoriesUseCase: GetSearchCategoriesUseCase,
    private val getAuthorsUseCase: GetAuthorsUseCase,
    private val getTopicsUseCase: GetTopicsUseCases,
    private val searchTopicsUseCase: SearchTopicsUseCase,
    private val searchAuthorsUseCase: SearchAuthorsUseCase,
    private val followNewsSourceUseCase: FollowNewsSourceUseCase,
    private val unfollowNewSourceUseCase: UnfollowNewSourceUseCase,
    val getSavedTopicsUseCase: GetSavedTopicsUseCase,
    val saveTopicUseCase: SaveTopicUseCase,
    val unSaveTopicUseCase: UnSaveTopicUseCase,
    val getSourceFollowedUseCase: GetFollowedSourcesUseCase,
) : ViewModel() {

    private val _searchedNews = MutableLiveData<UiState<ArrayList<ArticleUiModel>>>()
    val searchedNews: LiveData<UiState<ArrayList<ArticleUiModel>>> get() = _searchedNews

    private val _actionState = MutableLiveData<UiState<Unit>>()
    val actionState: LiveData<UiState<Unit>> get() = _actionState

    private val _searchCategories = MutableLiveData<List<Int>>()
    val searchCategories: LiveData<List<Int>> get() = _searchCategories

    private val _topics = MutableLiveData<UiState<List<TopicUiModel>>>()
    val topics: LiveData<UiState<List<TopicUiModel>>> = _topics

    private val _authors = MutableLiveData<List<AuthorUiModel>>()
    val authors: LiveData<List<AuthorUiModel>> = _authors

    private var cachedTopics: List<TopicModel> = emptyList()
    private var cachedAuthors: List<AuthorModel> = emptyList()

    private val _selectedCategory = MutableLiveData<Int>()
    val selectedCategory: LiveData<Int> = _selectedCategory

    var currentCategory = SearchCategory.NEWS.titleRes

    private var currentPage = 1
    private var currentPageSize = 10
    private var currentQuery: String = ""
    private var totalResult = 100
    private val newsList = ArrayList<ArticleUiModel>()
    private val sortByPublishedAt = "publishedAt"

    init {
        getSearchCategories()
        cachedAuthors = getAuthorsUseCase()
        cachedTopics = getTopicsUseCase()
        _selectedCategory.value = SearchCategory.NEWS.titleRes
        currentCategory = SearchCategory.NEWS.titleRes
        _searchedNews.value = UiState.Success(ArrayList())
        _topics.value = UiState.Success(emptyList())
        _authors.value = emptyList()
    }

    fun setCategory(category: Int) {
        _selectedCategory.value = category
        currentCategory = category
        if (currentQuery.isNotEmpty()) {
            search(currentQuery)
        } else {
            clearData()
        }
    }

    private fun getSearchCategories() {
        _searchCategories.value = getSearchCategoriesUseCase()
    }

    fun clearData() {
        _searchedNews.value = UiState.Success(ArrayList())
        newsList.clear()
        _topics.value = UiState.Success(emptyList())
        _authors.value = emptyList()

    }



    fun followNewsSource(sourceName: String, sourceImg: String, followerCount: Int) {
        _actionState.value = UiState.Loading
        val follow = FollowModel(sourceName, sourceImg, followerCount)
        viewModelScope.launch(Dispatchers.IO) {
            val result = followNewsSourceUseCase(follow)
            withContext(Dispatchers.Main) {
                if (result.isSuccess) {
                    _actionState.value = UiState.Success(Unit)
                    search(currentQuery)
                } else {
                    _actionState.value = UiState.Error(R.string.failed_follow)
                }
            }
        }
    }

    fun unFollowNewsSource(sourceName: String) {
        _actionState.value = UiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val result = unfollowNewSourceUseCase(sourceName)
            withContext(Dispatchers.Main) {
                if (result.isSuccess) {
                    _actionState.value = UiState.Success(Unit)
                    search(currentQuery)
                } else {
                    _actionState.value = UiState.Error(R.string.failed_unfollow)
                }
            }
        }
    }

    fun getTopics() {
        viewModelScope.launch(Dispatchers.IO) {
            cachedTopics = getTopicsUseCase()
        }
    }

    fun search(query: String) {
        val trimmed = query.trim()
        currentQuery = trimmed

        if (trimmed.isEmpty()) {
            clearData()
            return
        }

        when (currentCategory) {
            SearchCategory.NEWS.titleRes -> {
                searchNews(trimmed)
            }
            SearchCategory.TOPICS.titleRes -> {
                viewModelScope.launch(Dispatchers.IO) {
                    getSavedTopicsUseCase().collect { savedTopicsResult ->
                        if (savedTopicsResult.isSuccess) {
                            val savedTopics = savedTopicsResult.getOrNull() ?: emptyList()
                            val searchedTopics = searchTopicsUseCase(cachedTopics, trimmed)
                            val topicList = searchedTopics.map { topic ->
                                topic.toUi(savedTopics.contains(topic.topic))
                            }
                            withContext(Dispatchers.Main) {
                                _topics.value = UiState.Success(topicList)
                            }
                        } else {
                            withContext(Dispatchers.Main) {
                                _topics.value = UiState.Error(R.string.failed_to_topics)
                            }
                        }
                    }
                }
            }
            SearchCategory.AUTHORS.titleRes -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val searchedAuthors = searchAuthorsUseCase(cachedAuthors, trimmed)
                    val flow = getSourceFollowedUseCase()
                    flow.collect { result ->
                        withContext(Dispatchers.Main) {
                            if (result.isSuccess) {
                                val followedSources =
                                    result.getOrNull()?.map { it.sourceName } ?: emptyList()
                                val updatedAuthor = searchedAuthors.map { author ->
                                    author.toUi(followedSources.contains(author.sourceName))
                                }
                                _authors.value = updatedAuthor
                                _actionState.value = UiState.Success(Unit)
                            } else {
                                _authors.value = searchedAuthors.map { it.toUi(false) }
                                _actionState.value = UiState.Error(R.string.wrong_something)
                            }
                        }
                    }
                }
            }
        }
    }
    fun searchNews(query: String) {
        if (query != currentQuery) {
            currentPage = 1
            currentQuery = query
            newsList.clear()
        }
        fetchNews()
    }

    fun loadMoreNews() {
        if (newsList.size < totalResult) {
            currentPage++
            fetchNews()
        }
    }

    private fun fetchNews() {
        _searchedNews.value = UiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val result = getSearchedNewsUseCase(
                sortBy = sortByPublishedAt,
                pageSize = currentPageSize,
                query = currentQuery,
                page = currentPage,
            )
            withContext(Dispatchers.Main) {
                if (result.isSuccess) {
                    if (currentPage == 1) {
                        newsList.clear()
                    }
                    val data = result.getOrNull() ?: emptyList()
                    val news = data.map { it.toUi() }
                    newsList.addAll(news)
                    _searchedNews.value = UiState.Success(newsList)
                } else {
                    _searchedNews.value = UiState.Error(R.string.wrong_something)
                }
            }
        }
    }

    fun toggleFollowingBtn(author: AuthorUiModel) {
        _actionState.value = UiState.Loading
        if (author.isFollowed) {
            unFollowNewsSource(author.sourceName)
        } else {
            followNewsSource(author.sourceName, author.sourceImg, author.sourceFollowerCount)
        }
        viewModelScope.launch(Dispatchers.Main) {
            search(currentQuery)
        }
    }

    fun saveTopics(topicName: String) {
        _actionState.value = UiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val result = saveTopicUseCase(topicName)
            withContext(Dispatchers.Main) {
                if (result.isSuccess) {
                    _actionState.value = UiState.Success(Unit)
                    search(currentQuery)
                } else {
                    _actionState.value = UiState.Error(R.string.failed_to_save_topics)
                }
            }
        }
    }

    fun unSaveTopics(topicName: String) {
        _actionState.value = UiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val result = unSaveTopicUseCase(topicName)
            withContext(Dispatchers.Main) {
                if (result.isSuccess) {
                    _actionState.value = UiState.Success(Unit)
                    search(currentQuery)
                } else {
                    _actionState.value = UiState.Error(R.string.failed_to_unsave_topics)
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
}