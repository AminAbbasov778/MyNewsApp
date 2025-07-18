package com.example.mynewsapp.presentation.viewmodels

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynewsapp.R
import com.example.mynewsapp.domain.domainmodels.ProfileModel
import com.example.mynewsapp.domain.usecases.commonusecases.CapturePhotoUseCase
import com.example.mynewsapp.domain.usecases.commonusecases.GetProfileDataUseCase
import com.example.mynewsapp.domain.usecases.editprofileusecases.ConvertUriToBase64UseCase
import com.example.mynewsapp.domain.usecases.commonusecases.GetImagePickerOptionsUseCase
import com.example.mynewsapp.domain.usecases.editprofileusecases.UpdateUserProfileUseCase
import com.example.mynewsapp.presentation.mappers.toProfileUiModel
import com.example.mynewsapp.presentation.uimodels.profile.NewProfileUiModel
import com.example.mynewsapp.presentation.uimodels.profile.ProfileUiModel
import com.example.mynewsapp.presentation.uistates.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    val capturePhotoUseCase: CapturePhotoUseCase,
    val updateUserProfileUseCase: UpdateUserProfileUseCase,
    val getProfileDataUseCase: GetProfileDataUseCase,
    val convertUriToBase64UseCase: ConvertUriToBase64UseCase,
    val getImagePickerOptionsUseCase : GetImagePickerOptionsUseCase
) : ViewModel() {
    private var _imageUri = MutableLiveData<Uri>()
    val imageUri: LiveData<Uri> get() = _imageUri

    private var _imagePickerOptions = MutableLiveData<Array<String>>()
    val imagePickerOptions: LiveData<Array<String>> get() = _imagePickerOptions

    private var _updatedProfile = MutableLiveData<UiState<Int>>()
    val updatedProfile: LiveData<UiState<Int>> get() = _updatedProfile

    private var _profileData = MutableLiveData<UiState<ProfileUiModel>>()
    val profileData: LiveData<UiState<ProfileUiModel>> get() = _profileData


    init {
        getUserProfileData()
    }

    fun capturePhoto() {
        _imageUri.value = capturePhotoUseCase()
    }


    fun getImagePickerOptions() {
        _imagePickerOptions.value = getImagePickerOptionsUseCase()
    }

    fun updateUserProfile(
        imageUri: Uri?,
        bio: String,
        fullName: String,
        email: String,
        phoneNumber: String,
        website: String,
        userName: String,
    ) {
        _updatedProfile.value = UiState.Loading
        val imageBase64 = convertUriToBase64UseCase(imageUri)
        val profileModel = ProfileModel(fullName, bio,email,imageBase64 ?: "", userName, phoneNumber, website)
        viewModelScope.launch(Dispatchers.IO) {
            var result = updateUserProfileUseCase(profileModel)
            withContext(Dispatchers.Main) {
                _updatedProfile.value = if (result.isSuccess) {
                    UiState.Success(R.string.successful_updating_profile)
                } else {
                    UiState.Error(R.string.failure_updating_profile)
                }
            }
        }

    }

    fun getUserProfileData() {
            _profileData.value = UiState.Loading
            viewModelScope.launch(Dispatchers.IO) {
                val result =   getProfileDataUseCase()
                withContext(Dispatchers.Main) {
                    result.collect {profileData ->
                        if(profileData.isSuccess){
                            val  data = profileData.getOrNull()
                            data?.let {
                                _profileData.value =   UiState.Success(data.toProfileUiModel())
                            }
                        }else{
                            _profileData.value = UiState.Error(R.string.process_is_failure)
                        }
                    }

                }
            }



    }

}