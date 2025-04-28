package com.example.app.utils

import android.content.Context
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.facebook.appevents.internal.Constants

object ThemeHelper {



    fun isDarkMode(context: Context): Boolean {
        val sharedPref = context.getSharedPreferences(com.example.mynewsapp.Utils.Constants.PREF_NAME, Context.MODE_PRIVATE)
        return sharedPref.getBoolean(com.example.mynewsapp.Utils.Constants.KEY_DARK_MODE, false)
    }

    fun applyTheme(context: Context) {
        val isDark = isDarkMode(context)
        AppCompatDelegate.setDefaultNightMode(
            if (isDark) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )

    }


}
