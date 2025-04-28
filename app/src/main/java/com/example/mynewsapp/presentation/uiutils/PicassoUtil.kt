package com.example.mynewsapp.presentation.uiutils

import android.widget.ImageView
import com.example.mynewsapp.R
import com.squareup.picasso.Picasso

object PicassoUtil {

    fun ImageView.LoadUrl(url: String?) {
        Picasso.get().load(url).resize(300, 300).centerCrop().error(R.color.white)
            .placeholder(R.color.white).into(this)
    }
}