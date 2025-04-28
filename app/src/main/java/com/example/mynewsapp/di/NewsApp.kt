package com.example.mynewsapp.di

import android.app.Application
import android.content.Context
import com.example.mynewsapp.Utils.LanguageManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NewsApp : Application() {

        override fun attachBaseContext(base: Context?) {
            val sharedPreferences = base?.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
            val language = sharedPreferences?.getString("language", "en") ?: "en"
            val newContext = LanguageManager.setLocale(base!!, language)
            super.attachBaseContext(newContext)
        }


}
