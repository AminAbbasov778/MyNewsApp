package com.example.mynewsapp.domain.usecases.commonusecases

import android.net.Uri
import com.example.mynewsapp.domain.interfaces.CameraRepository
import javax.inject.Inject

class CapturePhotoUseCase @Inject constructor(val cameraRepository: CameraRepository) {
    operator fun invoke(): Uri = cameraRepository.createImgUri()

}