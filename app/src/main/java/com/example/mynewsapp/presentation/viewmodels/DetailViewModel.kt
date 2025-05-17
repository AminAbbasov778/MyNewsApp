package com.example.mynewsapp.presentation.viewmodels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynewsapp.R
import com.example.mynewsapp.domain.domainmodels.FollowModel
import com.example.mynewsapp.domain.usecases.commentusecases.GetCommentsUseCase
import com.example.mynewsapp.domain.usecases.detail.DeleteBookmarkUseCase
import com.example.mynewsapp.domain.usecases.detail.IsNewsBookmarkedUseCase
import com.example.mynewsapp.domain.usecases.detail.SaveBookmarkUseCase
import com.example.mynewsapp.domain.usecases.detailusecases.FavoriteNewsUseCase
import com.example.mynewsapp.domain.usecases.detailusecases.FollowNewsSourceUseCase
import com.example.mynewsapp.domain.usecases.detailusecases.GetFavoriteCountUseCase
import com.example.mynewsapp.domain.usecases.detailusecases.IsNewsFavoriteUseCase
import com.example.mynewsapp.domain.usecases.detailusecases.IsNewsSourceFollowedUseCase
import com.example.mynewsapp.domain.usecases.detailusecases.UnFavoriteNewUseCase
import com.example.mynewsapp.domain.usecases.detailusecases.UnfollowNewSourceUseCase
import com.example.mynewsapp.presentation.mappers.toDomain
import com.example.mynewsapp.presentation.mappers.toUi
import com.example.mynewsapp.presentation.uimodels.common.ArticleUiModel
import com.example.mynewsapp.presentation.uimodels.common.FollowUiModel
import com.example.mynewsapp.presentation.uistates.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val saveBookmarkUseCase: SaveBookmarkUseCase,
    private val deleteBookmarkUseCase: DeleteBookmarkUseCase,
    private val isNewsBookmarkedUseCase: IsNewsBookmarkedUseCase,
    private val followNewsSourceUseCase: FollowNewsSourceUseCase,
    private val unfollowNewSourceUseCase: UnfollowNewSourceUseCase,
    private val favoriteNewsUseCase: FavoriteNewsUseCase,
    private val unFavoriteNewsUseCase: UnFavoriteNewUseCase,
    private val isNewsFavoriteUseCase: IsNewsFavoriteUseCase,
    private val isNewsSourceFollowedUseCase: IsNewsSourceFollowedUseCase,
    private val getFavoriteCountUseCase: GetFavoriteCountUseCase,
    private val getCommentsUseCase: GetCommentsUseCase,
) : ViewModel() {

    private val _actionState = MutableLiveData<UiState<Unit>>()
    val actionState: LiveData<UiState<Unit>> get() = _actionState

    private val _isBookmarked = MutableLiveData<Boolean>(false)
    val isBookmarked: LiveData<Boolean> get() = _isBookmarked

    private val _isFollowing = MutableLiveData<Boolean>(false)
    val isFollowing: LiveData<Boolean> get() = _isFollowing

    private val _isFavorite = MutableLiveData<Boolean>(false)
    val isFavorite: LiveData<Boolean> get() = _isFavorite

    private val _favoriteCount = MutableLiveData<UiState<Int>>()
    val favoriteCount: LiveData<UiState<Int>> get() = _favoriteCount

    private val _commentCount = MutableLiveData<UiState<Int>>()
    val commentCount: LiveData<UiState<Int>> get() = _commentCount

    init {
        resetState()
    }

    fun resetState() {
        _actionState.value = UiState.Success(Unit)
        _isFavorite.value = false
        _favoriteCount.value = UiState.Success(0)
        _isBookmarked.value = false
        _isFollowing.value = false
        _commentCount.value = UiState.Success(0)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getComments(url: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val resultFlow =
                getCommentsUseCase(url).map { result -> result.map { list -> list.map { it.toUi() } } }
            resultFlow.collect { result ->
                withContext(Dispatchers.Main) {
                    if (result.isSuccess) {
                        val commentCount = result.getOrNull()?.size ?: 0
                        _commentCount.value = UiState.Success(commentCount)
                    } else {
                        _commentCount.value = UiState.Error(R.string.failed_to_fetch_comment_count)
                    }
                }
            }
        }
    }

    fun getFavoriteStatus(url: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _actionState.postValue(UiState.Loading)
            val isFavoriteFlow = isNewsFavoriteUseCase(url)
            val favoriteCountFlow = getFavoriteCountUseCase(url)

            isFavoriteFlow.collectLatest { isFavoriteResult ->
                favoriteCountFlow.collectLatest { countResult ->
                    withContext(Dispatchers.Main) {
                        if (isFavoriteResult.isSuccess && countResult.isSuccess) {
                            val isFavorite = isFavoriteResult.getOrNull() ?: false
                            val count = countResult.getOrNull() ?: 0
                            _isFavorite.value = isFavorite
                            _favoriteCount.value = UiState.Success(count)
                            _actionState.value = UiState.Success(Unit)
                        } else {
                            _actionState.value = UiState.Error(R.string.wrong_something)
                        }
                    }
                }
            }
        }
    }

    fun insertBookmark(news: ArticleUiModel) {
        _actionState.value = UiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val result = saveBookmarkUseCase(news.toDomain())
            withContext(Dispatchers.Main) {
                if (result.isSuccess) {
                    _isBookmarked.value = true
                    _actionState.value = UiState.Success(Unit)
                } else {
                    _actionState.value = UiState.Error(R.string.failed_add_bookmark)
                }
            }
        }
    }


    fun deleteBookmark(article: ArticleUiModel) {
        _actionState.value = UiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val result = deleteBookmarkUseCase(article.url!!)
            withContext(Dispatchers.Main) {
                if (result.isSuccess) {
                    _isBookmarked.value = false
                    _actionState.value = UiState.Success(Unit)
                } else {
                    _actionState.value = UiState.Error(R.string.failed_remove_from_bookmark)
                }
            }
        }
    }

    fun getIsNewsBookmarked(article: ArticleUiModel) {
        _actionState.value = UiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val result = article.url?.let { isNewsBookmarkedUseCase(it) }
            withContext(Dispatchers.Main) {
                if (result != null && result.isSuccess) {
                    _isBookmarked.value = result.getOrNull()!!
                    _actionState.value = UiState.Success(Unit)
                } else {
                    _actionState.value = UiState.Error(R.string.wrong_something)
                }
            }
        }
    }

    fun isNewsSourceFollowed(sourceName: String) {
        _actionState.value = UiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val result = isNewsSourceFollowedUseCase(sourceName)
            withContext(Dispatchers.Main) {
                if (result.isSuccess) {
                    _isFollowing.value = result.getOrNull()
                    _actionState.value = UiState.Success(Unit)
                } else {
                    _actionState.value = UiState.Error(R.string.wrong_something)
                }
            }
        }
    }

    fun followNewsSource(sourceName: String, sourceImg: String, followerCount: Int) {
        _actionState.value = UiState.Loading
        val follow = FollowModel(sourceName, sourceImg, followerCount)
        viewModelScope.launch(Dispatchers.IO) {
            val result = followNewsSourceUseCase(follow)
            withContext(Dispatchers.Main) {
                if (result.isSuccess) {
                    _isFollowing.value = true
                    _actionState.value = UiState.Success(Unit)
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
                    _isFollowing.value = false
                    _actionState.value = UiState.Success(Unit)
                } else {
                    _actionState.value = UiState.Error(R.string.failed_unfollow)
                }
            }
        }
    }

    fun favoriteNews(url: String) {
        _actionState.value = UiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val result = favoriteNewsUseCase(url)
            withContext(Dispatchers.Main) {
                if (result.isSuccess) {
                    _isFavorite.value = true
                    _actionState.value = UiState.Success(Unit)
                } else {
                    _actionState.value = UiState.Error(R.string.failed_favorite)
                }
            }
        }
    }

    fun unFavoriteNews(url: String) {
        _actionState.value = UiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val result = unFavoriteNewsUseCase(url)
            withContext(Dispatchers.Main) {
                if (result.isSuccess) {
                    _isFavorite.value = false
                    _actionState.value = UiState.Success(Unit)
                } else {
                    _actionState.value = UiState.Error(R.string.failed_unfavorite)
                }
            }
        }
    }

    fun toggleFollowingBtn(sourceName: String, sourceLogo: String, followerCount: Int) {
        if (_isFollowing.value == true) unFollowNewsSource(sourceName)
        else followNewsSource(sourceName, sourceLogo, followerCount)
    }

    fun toggleFavorite(url: String) {
        if (_isFavorite.value == true) unFavoriteNews(url)
        else favoriteNews(url)
    }

    fun toggleBookmark(news: ArticleUiModel) {
        if (_isBookmarked.value == true) deleteBookmark(news)
        else insertBookmark(news)
    }

    fun favoriteStatus() {
        _isFavorite.value = false
    }
}