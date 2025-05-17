package com.example.mynewsapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynewsapp.R
import com.example.mynewsapp.domain.domainmodels.ArticleModel
import com.example.mynewsapp.domain.domainmodels.FollowModel
import com.example.mynewsapp.domain.usecases.commonusecases.GetFollowedSourcesUseCase
import com.example.mynewsapp.domain.usecases.detailusecases.UnfollowNewSourceUseCase
import com.example.mynewsapp.domain.usecases.followingusecases.GetSearchedFollowSourcesUseCase
import com.example.mynewsapp.presentation.mappers.toUi
import com.example.mynewsapp.presentation.uimodels.common.ArticleUiModel
import com.example.mynewsapp.presentation.uimodels.common.FollowUiModel
import com.example.mynewsapp.presentation.uistates.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FollowingViewModel @Inject constructor(
    val getFollowedSourcesUseCase: GetFollowedSourcesUseCase,
    val unfollowNewSourceUseCase: UnfollowNewSourceUseCase,
    val getSearchedFollowSourcesUseCase: GetSearchedFollowSourcesUseCase
    ) :
    ViewModel() {

    private val _followedSources = MutableLiveData<UiState<List<FollowUiModel>>>()
    val followedSources: LiveData<UiState<List<FollowUiModel>>> get() = _followedSources

    private var _followState = MutableLiveData<UiState<Unit>>()
    val followState : LiveData<UiState<Unit>> get() = _followState

    private val _searchedSources = MutableLiveData<UiState<ArrayList<FollowUiModel>>>()
    val searchedSources: LiveData<UiState<ArrayList<FollowUiModel>>> = _searchedSources


    private var cachedSources: List<FollowModel> = emptyList()

    init {
        getFollowedSources()
    }

    fun getFollowedSources() {
        _followedSources.value = UiState.Loading

        viewModelScope.launch {
            getFollowedSourcesUseCase().collect { result ->
                if (result.isSuccess) {
                    val sources = result.getOrNull()
                        cachedSources = sources ?: emptyList()
                    _followedSources.value = UiState.Success(sources?.map { it.toUi() } ?: emptyList())


                } else {
                    _followedSources.value =
                        UiState.Error(R.string.failed_to_load_followed_sources)
                }
            }
        }
    }

    fun unfollowNewsSource(sourceName: String) {
        _followState.value = UiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val result = unfollowNewSourceUseCase(sourceName)
            withContext(Dispatchers.Main) {
                if (result.isSuccess) {
                    _followState.value = UiState.Success(Unit)
                } else {
                    _followState.value = UiState.Error(R.string.failed_unfollow)
                }
            }
        }

    }

    fun searchNews(query: String) {
        val trimmedQuery =query.trim()
        if(trimmedQuery.isNotEmpty()){
            _searchedSources.value = UiState.Loading
            viewModelScope.launch(Dispatchers.Default) {
                val filtered = getSearchedFollowSourcesUseCase(cachedSources, trimmedQuery)
                val uiList = filtered.map { it.toUi() }
                withContext(Dispatchers.Main) {
                    _searchedSources.value = UiState.Success(ArrayList(uiList))
                }
            }
        }else{
            _searchedSources.value = UiState.Success(ArrayList(cachedSources.map { it.toUi() }))

        }

    }
}