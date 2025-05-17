package com.example.mynewsapp.domain.usecases.commonusecases

import com.example.mynewsapp.domain.interfaces.EditProfileRepository
import javax.inject.Inject

class GetImagePickerOptionsUseCase @Inject constructor(val editProfileRepository: EditProfileRepository) {
    operator fun invoke(): Array<String>  = editProfileRepository.getImagePickerOptions()
}