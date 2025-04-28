package com.example.mynewsapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynewsapp.R
import com.example.mynewsapp.data.model.latestnews.Article
import com.example.mynewsapp.domain.usecases.commonusecases.GetCategoryUseCase
import com.example.mynewsapp.domain.usecases.commonusecases.GetProcessedNewsUseCase
import com.example.mynewsapp.presentation.uistates.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getProcessedNewsUseCase: GetProcessedNewsUseCase,
    private val getCategoryUseCase: GetCategoryUseCase,
) : ViewModel() {

    private val _latestNewsState = MutableLiveData<UiState<ArrayList<Article>>>()
    val latestNewsState: LiveData<UiState<ArrayList<Article>>> get() = _latestNewsState

    private val _trendingNewsState = MutableLiveData<UiState<List<Article>>>()
    val trendingNewsState: LiveData<UiState<List<Article>>> get() = _trendingNewsState

        private val _categoryList = MutableLiveData<ArrayList<String>>()
        val categoryList: LiveData<ArrayList<String>> get() = _categoryList

        private var _isSeeAllActive = MutableLiveData<Boolean>(false)
        val isSeeAllActive : LiveData<Boolean> get() = _isSeeAllActive

        private var currentCategory = "all"
    private var currentPage = 1
    private val pageSize = 5
    private var totalResult = 10
    private val newsList = ArrayList<Article>()

    init {
        getCategories()
        getTrendingNewsResult()
        getLatestNewsResult()
    }

    fun getLatestNewsResult(totalNewsCount: Int = totalResult, category: String = currentCategory) {
        totalResult = totalNewsCount
        currentCategory = category
        currentPage = 1
        newsList.clear()
        fetchNews()
    }

    fun loadMoreNews() {
        if (newsList.size < totalResult) {
            currentPage++
            fetchNews()
        }
    }

    fun toggleSeeAll(){
        if(isSeeAllActive.value!!) getLatestNewsResult(10)
        else getLatestNewsResult(100)
        _isSeeAllActive.value = !isSeeAllActive.value!!
    }

     fun fetchNews() {
        _latestNewsState.value = UiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val result =
                getProcessedNewsUseCase(currentCategory, "publishedAt", pageSize, currentPage)

            withContext(Dispatchers.Main){
               if(result.isSuccess){
                   val data = result.getOrNull() ?: emptyList()
                   if (currentPage == 1) {
                       newsList.clear()
                       newsList.addAll(data.take(pageSize))
                   } else {
                       newsList.addAll(data.take(totalResult - newsList.size))
                   }
                   _latestNewsState.value = UiState.Success(ArrayList(newsList))

                }
                else{
                    _latestNewsState.value = UiState.Error(R.string.wrong_something)
               }
            }

        }
    }

    private fun getTrendingNewsResult() {
        _trendingNewsState.value = UiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val result = getProcessedNewsUseCase("everything", "popularity")
            withContext(Dispatchers.Main){
                if(result.isSuccess){
                    val data = result.getOrNull() ?: emptyList()
                    _trendingNewsState.value = UiState.Success(data)
                }
                else{
                    _trendingNewsState.value = UiState.Error(R.string.wrong_something)
                }


            }

        }
    }

    private fun getCategories() {
        _categoryList.value = getCategoryUseCase()
    }
}