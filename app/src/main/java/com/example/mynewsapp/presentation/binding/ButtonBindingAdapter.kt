package com.example.mynewsapp.presentation.binding

import android.content.res.ColorStateList
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.example.mynewsapp.R
import com.google.android.material.button.MaterialButton

object ButtonBindingAdapter {

    @BindingAdapter("followStyle")
    @JvmStatic
    fun MaterialButton.setFollowStyle(isFollowed: Boolean?) {
        isFollowed?.let {
            if (it) {
                setText(R.string.following)
                setTextColor(ContextCompat.getColor(context, R.color.btnColor))
                strokeWidth = 0
                backgroundTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(context, R.color.primary_blue))
            } else {
                setText(R.string.follow)
                setTextColor(ContextCompat.getColor(context, R.color.primary_blue))
                strokeWidth = 7
                strokeColor =
                    ColorStateList.valueOf(ContextCompat.getColor(context, R.color.primary_blue))
                backgroundTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white))
            }
        }
    }


}