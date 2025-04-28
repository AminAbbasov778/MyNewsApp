package com.example.mynewsapp.domain.usecases.createnewsusecases

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant

class GetTimeStampUseCase  {
    @RequiresApi(Build.VERSION_CODES.O)
    operator fun invoke() : String = Instant.now().toString()
}