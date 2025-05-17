package com.example.mynewsapp.data.repositories

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.example.mynewsapp.domain.interfaces.CameraRepository
import java.io.File
import javax.inject.Inject

class CameraRepositoryImpl @Inject constructor(val context: Context) : CameraRepository {
    override fun createImgUri(): Uri {
        val file = File.createTempFile("IMG_", ".jpg", context.cacheDir)
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
    }

    override fun getImagePickerOptions(): Array<String> = arrayOf("Camera", "Gallery")

}