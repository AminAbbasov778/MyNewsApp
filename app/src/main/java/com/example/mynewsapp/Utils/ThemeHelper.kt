package com.example.app.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatDelegate
import com.example.mynewsapp.presentation.activities.MainActivity

object ThemeHelper {

    fun isDarkMode(context: Context): Boolean {
        val sharedPref = context.getSharedPreferences(
            com.example.mynewsapp.Utils.Constants.PREF_NAME, Context.MODE_PRIVATE
        )
        return sharedPref.getBoolean(
            com.example.mynewsapp.Utils.Constants.KEY_DARK_MODE, false
        )
    }

    fun applyTheme(context: Context) {
        val isDark = isDarkMode(context)
        AppCompatDelegate.setDefaultNightMode(
            if (isDark) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO

        )
    }

    fun restartApp(activity: Activity) {
        val intent = Intent(activity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        activity.startActivity(intent)
        activity.overridePendingTransition(0, 0)
    }



}
