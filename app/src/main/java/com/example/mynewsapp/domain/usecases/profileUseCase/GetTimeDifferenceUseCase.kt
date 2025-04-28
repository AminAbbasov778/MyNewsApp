package com.example.mynewsapp.domain.usecases.profileUseCase

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.mynewsapp.domain.usecases.commonusecases.TimeDifferenceUseCase
import javax.inject.Inject

class GetTimeDifferenceUseCase @Inject constructor(
    val changeIsoToMillisForUserNewsUseCase: ChangeIsoToMillisFromFirebaseUseCase,
    val timeDifferenceUseCase: TimeDifferenceUseCase
) {
    @RequiresApi(Build.VERSION_CODES.O)
    operator fun invoke(publishedAt : String): String {
        return timeDifferenceUseCase(changeIsoToMillisForUserNewsUseCase(publishedAt))
    }

}