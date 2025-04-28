package com.example.mynewsapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynewsapp.R
import com.example.mynewsapp.data.model.latestnews.Article
import com.example.mynewsapp.domain.usecases.commonusecases.GetProcessedNewsUseCase
import com.example.mynewsapp.presentation.uistates.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TrendingViewModel @Inject constructor(val getProcessedNewsUseCase: GetProcessedNewsUseCase) :
    ViewModel() {

    private var _trendingNews = MutableLiveData<UiState<List<Article>>>(UiState.Loading)
    val trendingNews: LiveData<UiState<List<Article>>> get() = _trendingNews

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
                    _trendingNews.value = UiState.Success(data)
                }
                else{
                      _trendingNews.value = UiState.Error(R.string.wrong_something)
                }

            }
        }
    }
}