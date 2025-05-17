package com.example.mynewsapp.presentation.viewmodels

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynewsapp.R
import com.example.mynewsapp.domain.usecases.commonusecases.GetFollowedSourcesUseCase
import com.example.mynewsapp.domain.usecases.commonusecases.GetProfileDataUseCase
import com.example.mynewsapp.domain.usecases.profileUseCase.DeleteNewsByPublishedAtUseCase
import com.example.mynewsapp.domain.usecases.profileUseCase.GetTimeDifferenceUseCase
import com.example.mynewsapp.domain.usecases.profileUseCase.GetUserNewsUseCase
import com.example.mynewsapp.presentation.mappers.toProfileUiModel
import com.example.mynewsapp.presentation.mappers.toUi
import com.example.mynewsapp.presentation.uimodels.createnews.UserNewsUiModel
import com.example.mynewsapp.presentation.uimodels.profile.ProfileUiModel
import com.example.mynewsapp.presentation.uistates.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class ProfileViewModel @Inject constructor(
    val getProfileDataUseCase: GetProfileDataUseCase,
    val getUserNewsUseCase: GetUserNewsUseCase,
    val getTimeDifferenceUseCase: GetTimeDifferenceUseCase,
    val deleteNewsByPublishedAtUseCase: DeleteNewsByPublishedAtUseCase,
    val getFollowedSourcesUseCase: GetFollowedSourcesUseCase,
) :
    ViewModel() {
    private var _profileData = MutableLiveData<UiState<ProfileUiModel>>()
    val profileData: LiveData<UiState<ProfileUiModel>> get() = _profileData


    private var _userNews = MutableLiveData<UiState<List<UserNewsUiModel>>>()
    val userNews: LiveData<UiState<List<UserNewsUiModel>>> get() = _userNews

    private var _isNewsDeleted = MutableLiveData<UiState<Int>>()
    val isNewsDeleted: LiveData<UiState<Int>> get() = _isNewsDeleted

    private val _followedSourcesCount = MutableLiveData<UiState<Int>>()
    val followedSourcesCount: LiveData<UiState<Int>> get() = _followedSourcesCount

    init {
        getUserNews()
        getProfileData()
        getFollowedSources()

    }


    fun getProfileData() {
        _profileData.value = UiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val result = getProfileDataUseCase()
            withContext(Dispatchers.Main) {
                result.collect { profileData ->
                    if (profileData.isSuccess) {
                        val data = profileData.getOrNull()
                        data?.let {
                            _profileData.value = UiState.Success(
                                data.toProfileUiModel()
                            )
                        }
                    } else {
                        _profileData.value = UiState.Error(R.string.process_is_failure)
                    }
                }

            }
        }

    }




    @RequiresApi(Build.VERSION_CODES.O)
    fun getUserNews() {
        viewModelScope.launch(Dispatchers.IO) {
            var result = getUserNewsUseCase()
            result.collect { userNews ->
            withContext(Dispatchers.Main) {
                    if (userNews.isSuccess) {
                        var newsResult = userNews.getOrNull()?.map {
                            it.toUi(getTimeDifferenceUseCase(it.publishedAt))
                        }
                        _userNews.value = UiState.Success(newsResult ?: emptyList())
                    } else {
                        _userNews.value = UiState.Error(R.string.process_is_failure_to_get_news)
                    }
                }

            }
        }
    }


    fun deleteNewsByPublishedAt(publishedAt: String) {
        _isNewsDeleted.value = UiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val result = deleteNewsByPublishedAtUseCase(publishedAt)
            withContext(Dispatchers.Main) {
                _isNewsDeleted.value =
                    if (result.isSuccess) UiState.Success(R.string.successfully_deleted) else UiState.Error(
                        R.string.failure_deleting
                    )
            }
        }

    }


     fun getFollowedSources() {
        _followedSourcesCount.value = UiState.Loading

        viewModelScope.launch {
            getFollowedSourcesUseCase().collect { result ->
                if (result.isSuccess) {
                   val list = result.getOrNull()?.map { it.toUi() }
                    _followedSourcesCount.value = UiState.Success(list?.size ?: 0)
                } else {
                    _followedSourcesCount.value =
                        UiState.Error(R.string.failed_to_load_followed_sources)
                }
            }
        }
    }

    fun getWebsiteUrl(): String? {
        val website = (profileData.value as? UiState.Success)?.data?.website
        return if (!website.isNullOrBlank() && isValidUrl(website)) {
            website
        } else {
            null
        }
    }
    private fun isValidUrl(url: String): Boolean {
        return try {
            val uri = Uri.parse(url)
            uri.scheme == "http" || uri.scheme == "https"
        } catch (e: Exception) {
            false
        }
    }




}