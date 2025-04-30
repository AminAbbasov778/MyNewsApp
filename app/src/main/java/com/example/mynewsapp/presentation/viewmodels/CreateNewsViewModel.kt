package com.example.mynewsapp.presentation.viewmodels

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynewsapp.R
import com.example.mynewsapp.data.model.userprofile.Profile
import com.example.mynewsapp.domain.usecases.commonusecases.CapturePhotoUseCase
import com.example.mynewsapp.domain.usecases.createnewsusecases.CreateNewsUseCase
import com.example.mynewsapp.domain.usecases.createnewsusecases.UserNewsModelUseCase
import com.example.mynewsapp.domain.usecases.commonusecases.GetProfileDataUseCase
import com.example.mynewsapp.presentation.uistates.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CreateNewsViewModel @Inject constructor(
    val capturePhotoUseCase: CapturePhotoUseCase,
    val createNewsUseCase: CreateNewsUseCase,
    val userNewsModelUseCase: UserNewsModelUseCase,
    val getProfileDataUseCase: GetProfileDataUseCase,
) : ViewModel() {
    private var _imageUri = MutableLiveData<Uri>()
    val imageUri: LiveData<Uri> get() = _imageUri

    private var _createNewsResult = MutableLiveData<UiState<Int>>()
    val createNewsResult: LiveData<UiState<Int>> get() = _createNewsResult


    private val _title = MutableStateFlow("")
    private val _article = MutableStateFlow("")
    private val _uri = MutableStateFlow("")


    private var _isFormValid: StateFlow<Boolean> =
        combine(_title, _article, _uri) { title, desc, uri ->
            title.isNotBlank() && desc.isNotBlank() && uri.isNotBlank()
        }.stateIn(viewModelScope, SharingStarted.Lazily, false)

    val isFormValid: StateFlow<Boolean> get() = _isFormValid

    private var _isPublishedActive = MutableLiveData<Boolean>()
    val isPublishedActive : LiveData<Boolean> get() = _isPublishedActive

    fun togglePublishedBtn(){
        _isPublishedActive.value = isFormValid.value
    }

    fun onTitleChanged(text: String) {
        _title.value = text
    }

    fun onDescriptionChanged(text: String) {
        _article.value = text
    }

    fun onImageChanged(uri: String) {
        _uri.value = uri
    }

    private var _imagePickerOptions = MutableLiveData<Array<String>>()
    val imagePickerOptions: LiveData<Array<String>> get() = _imagePickerOptions

    fun capturePhoto() {
        _imageUri.value = capturePhotoUseCase()
    }

    fun getImagePickerOptions() {
        _imagePickerOptions.value = arrayOf("Camera", "Gallery")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createNews(newsTitle: String, newsArticle: String, newsImageUri: Uri) {
        _createNewsResult.value = UiState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            val profileData = getUserProfileData()
            if (profileData == null) {
                withContext(Dispatchers.Main) {
                    _createNewsResult.value = UiState.Error(R.string.failure_created_news)
                }
                return@launch
            }

            val fullName = profileData.fullName
            val profileImageBase64 = profileData.imageBase64

            val userNews = userNewsModelUseCase(
                newsTitle,
                newsArticle,
                newsImageUri,
                fullName,
                profileImageBase64
            )

            val result = createNewsUseCase(userNews)

            withContext(Dispatchers.Main) {
                _createNewsResult.value = if (result.isSuccess) {
                    UiState.Success(R.string.successful_created_news)
                } else {
                    UiState.Error(R.string.failure_created_news)
                }
            }
        }
    }

    suspend fun getUserProfileData(): Profile? {
        val profileResult = getProfileDataUseCase()
       return profileResult.firstOrNull{it.isSuccess}?.getOrNull()
    }
}

