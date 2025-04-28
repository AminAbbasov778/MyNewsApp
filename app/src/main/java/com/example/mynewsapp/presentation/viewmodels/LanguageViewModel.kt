package com.example.mynewsapp.presentation.viewmodels

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynewsapp.R
import com.example.mynewsapp.Utils.LanguageManager
import com.example.mynewsapp.data.model.language.LanguageModel
import com.example.mynewsapp.domain.usecases.languageusecases.GetCurrentLanguageUseCase
import com.example.mynewsapp.domain.usecases.languageusecases.GetLanguageListUseCase
import com.example.mynewsapp.domain.usecases.languageusecases.SetLanguageUseCase
import com.example.mynewsapp.presentation.activities.MainActivity
import com.example.mynewsapp.presentation.uimodels.language.LanguageUiModel
import com.example.mynewsapp.presentation.uistates.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LanguageViewModel @Inject constructor(
    val getLanguageListUseCase: GetLanguageListUseCase,
    val getCurrentLanguageUseCase: GetCurrentLanguageUseCase,
    val setLanguageUseCase: SetLanguageUseCase,
    @ApplicationContext private val context: Context,
) : ViewModel() {

    private var _languageList = MutableLiveData<ResultState<List<LanguageUiModel>>>()
    val languageList: LiveData<ResultState<List<LanguageUiModel>>> get() = _languageList
    private var langList = listOf<LanguageModel>()

    init {
        getLanguageList()
        getCurrentLanguage()
    }

    fun getLanguageList() {
        langList = getLanguageListUseCase()
    }

    fun setIsSelected(langCode: String) {
        val list = langList.map { LanguageUiModel(it.language, it.code, it.code == langCode) }
        _languageList.value = ResultState.Success(list)
    }

    fun getCurrentLanguage() {
        val result = getCurrentLanguageUseCase()
        if (result.isSuccess) {
            setIsSelected(result.getOrNull() ?: "en")
        } else {
            _languageList.value = ResultState.Error(R.string.failed_to_fetch_current_lang)
        }
    }

    fun setLanguage(language: LanguageUiModel) {
        viewModelScope.launch {
            val result = setLanguageUseCase(language.code)
            if (result.isSuccess) {
                setIsSelected(language.code)
                LanguageManager.setLocale(context, language.code)

                val intent = Intent(context, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)

            } else {
                _languageList.value = ResultState.Error(R.string.failed_to_set_lang)
            }
        }
    }






}