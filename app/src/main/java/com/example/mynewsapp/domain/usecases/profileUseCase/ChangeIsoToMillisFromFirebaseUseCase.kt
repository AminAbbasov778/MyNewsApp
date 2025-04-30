package com.example.mynewsapp.domain.usecases.profileUseCase

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import javax.inject.Inject

class ChangeIsoToMillisFromFirebaseUseCase  @Inject constructor() {
    @RequiresApi(Build.VERSION_CODES.O)
    operator fun invoke(time: String): Long {
        return Instant.parse(time).toEpochMilli()
    }
}