package com.example.mynewsapp.domain.interfaces

import android.net.Uri

interface CameraRepository {

    fun createImgUri(): Uri
}