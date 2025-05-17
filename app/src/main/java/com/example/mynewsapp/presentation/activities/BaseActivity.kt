package com.example.mynewsapp.presentation.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.example.mynewsapp.Utils.LanguageManager

abstract class BaseActivity : AppCompatActivity() {
    override fun attachBaseContext(newBase: Context?) {
        val sharedPreferences = newBase?.getSharedPreferences("sharedpref", Context.MODE_PRIVATE)
        val lang = sharedPreferences?.getString("language", "en") ?: "en"
        val context =  newBase?.let { LanguageManager.setLocale(it, lang)}
        super.attachBaseContext(context)
    }
}