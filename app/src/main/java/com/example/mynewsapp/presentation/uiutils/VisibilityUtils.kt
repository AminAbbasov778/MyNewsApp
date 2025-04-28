package com.example.mynewsapp.presentation.uiutils

import android.view.View

object VisibilityUtils {
    fun View.show() {
        this.visibility = View.VISIBLE
    }

    fun View.setGone() {
        this.visibility = View.GONE
    }


}