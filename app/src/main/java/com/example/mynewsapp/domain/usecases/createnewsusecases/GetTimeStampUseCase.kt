package com.example.mynewsapp.domain.usecases.createnewsusecases

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.mynewsapp.domain.interfaces.UserRepository
import java.time.Instant
import javax.inject.Inject

class GetTimeStampUseCase @Inject constructor() {
    @RequiresApi(Build.VERSION_CODES.O)
    operator fun invoke() : String = Instant.now().toString()
}