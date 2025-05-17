package com.example.mynewsapp.presentation.enums

import android.support.annotation.StringRes
import com.example.mynewsapp.R

enum class SearchCategory(@StringRes val titleRes: Int) {
    NEWS(R.string.news),
    TOPICS(R.string.topic),
    AUTHORS(R.string.author)
}