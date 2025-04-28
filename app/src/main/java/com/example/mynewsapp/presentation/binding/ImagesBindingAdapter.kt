package com.example.mynewsapp.presentation.binding

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.example.mynewsapp.R
import com.squareup.picasso.Picasso

object ImagesBindingAdapter {

    @BindingAdapter("imageResource")
    @JvmStatic
    fun setImageResource(imageView: ImageView, resource: Int) {
        imageView.setImageResource(resource)


    }

    @BindingAdapter("loadUrl")
    @JvmStatic
    fun set(imageView: ImageView, url: String?) {
        if (!url.isNullOrEmpty()) Picasso.get().load(url).resize(300, 300).centerCrop()
            .error(R.color.white).placeholder(R.color.white).into(imageView)

    }
    @BindingAdapter("setImageBitmap")
    @JvmStatic
    fun setImageBitmap(imageView: ImageView, bitmap: Bitmap?) {
        bitmap?.let {
            imageView.setImageBitmap(it)
        }
    }

}