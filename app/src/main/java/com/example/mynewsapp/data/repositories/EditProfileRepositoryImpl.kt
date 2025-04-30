package com.example.mynewsapp.data.repositories

import com.example.mynewsapp.domain.interfaces.EditProfileRepository
import javax.inject.Inject

class EditProfileRepositoryImpl @Inject constructor() : EditProfileRepository{
    override fun getImagePickerOptions(): Array<String> = arrayOf("Camera", "Gallery")
}