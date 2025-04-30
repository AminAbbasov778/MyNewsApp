package com.example.mynewsapp.presentation.uiutils

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import com.example.mynewsapp.databinding.PopupMenuBinding

class NewsPopupHelper(
    private val context: Context,
    private val anchorView: View,
    private val onDeleteClick: () -> Unit,

    ) {

    fun showPopup() {
        val binding = PopupMenuBinding.inflate(LayoutInflater.from(context))

        val popupWindow = PopupWindow(
            binding.root,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        popupWindow.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        popupWindow.elevation = 10f


        binding.deleteConstraint.setOnClickListener {
            onDeleteClick()
            popupWindow.dismiss()
        }

        popupWindow.showAsDropDown(anchorView)
    }
}

