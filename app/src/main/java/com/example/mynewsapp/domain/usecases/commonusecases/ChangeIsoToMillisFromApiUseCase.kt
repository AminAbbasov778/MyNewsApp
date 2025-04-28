package com.example.mynewsapp.domain.usecases.commonusecases

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

class ChangeIsoToMillisFromApiUseCase @Inject constructor() {
    operator fun invoke(isoTime: String): Long {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        format.timeZone = TimeZone.getTimeZone("UTC")
        val parsedTime = format.parse(isoTime)
        return parsedTime?.time ?: 0L
    }
}