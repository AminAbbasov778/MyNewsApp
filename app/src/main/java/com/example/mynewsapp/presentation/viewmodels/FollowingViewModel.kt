package com.example.mynewsapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynewsapp.R
import com.example.mynewsapp.domain.usecases.commonusecases.GetFollowedSourcesUseCase
import com.example.mynewsapp.domain.usecases.detailusecases.UnfollowNewSourceUseCase
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
    val unfollowNewSourceUseCase: UnfollowNewSourceUseCase
    ) :
    ViewModel() {

    private val _followedSources = MutableLiveData<UiState<List<FollowUiModel>>>()
    val followedSources: LiveData<UiState<List<FollowUiModel>>> get() = _followedSources

    private var _followState = MutableLiveData<UiState<Unit>>()
    val followState : LiveData<UiState<Unit>> get() = _followState

    init {
        getFollowedSources()
    }

    fun getFollowedSources() {
        _followedSources.value = UiState.Loading

        viewModelScope.launch {
            getFollowedSourcesUseCase().collect { result ->
                if (result.isSuccess) {
                    val list = result.getOrNull()
                        ?.map { FollowUiModel(it.sourceName, it.sourceImg, it.sourceFollowerCount) }
                    _followedSources.value = UiState.Success(list ?: emptyList())


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
}