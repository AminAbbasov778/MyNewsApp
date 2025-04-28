package com.example.mynewsapp.domain.usecases.commonusecases

import android.content.Context
import com.example.mynewsapp.R
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class TimeDifferenceUseCase @Inject constructor(private val context: Context) {

    operator fun invoke(timeMillis: Long): String {
        val difference = System.currentTimeMillis() - timeMillis
        return when {
            difference < TimeUnit.SECONDS.toMillis(60) -> {
                val seconds = maxOf(1, TimeUnit.MILLISECONDS.toSeconds(difference))
                context.getString(R.string.time_ago_seconds, seconds)
            }
            difference < TimeUnit.MINUTES.toMillis(1) -> context.getString(R.string.time_ago_minutes, 1)
            difference < TimeUnit.HOURS.toMillis(1) -> context.getString(R.string.time_ago_minutes, TimeUnit.MILLISECONDS.toMinutes(difference))
            difference < TimeUnit.DAYS.toMillis(1) -> context.getString(R.string.time_ago_hours, TimeUnit.MILLISECONDS.toHours(difference))
            difference < TimeUnit.DAYS.toMillis(7) -> context.getString(R.string.time_ago_days, TimeUnit.MILLISECONDS.toDays(difference))
            difference < TimeUnit.DAYS.toMillis(30) -> context.getString(R.string.time_ago_weeks, difference / TimeUnit.DAYS.toMillis(7))
            difference < TimeUnit.DAYS.toMillis(365) -> context.getString(R.string.time_ago_months, difference / TimeUnit.DAYS.toMillis(30))
            else -> context.getString(R.string.time_ago_years, difference / TimeUnit.DAYS.toMillis(365))
        }
    }
}