package com.example.mynewsapp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.mynewsapp.databinding.UserNewsItemBinding
import com.example.mynewsapp.presentation.uimodels.createnews.UserNewsUiModel
import com.example.mynewsapp.presentation.uiutils.GenericDiffUtil
import com.example.mynewsapp.presentation.uiutils.NewsPopupHelper

class UserNewsAdapter(val onDeleteClick : (String) -> Unit) : RecyclerView.Adapter<UserNewsAdapter.UserNewsViewHolder>() {

    var userNewsList = ArrayList<UserNewsUiModel>()

    inner class UserNewsViewHolder(val binding: UserNewsItemBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): UserNewsViewHolder {
        val binding =
            UserNewsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserNewsViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: UserNewsViewHolder,
        position: Int,
    ) {
       holder.binding.usernews = userNewsList[position]
        popupMenu(holder,userNewsList[position])
    }

    override fun getItemCount(): Int {
        return userNewsList.size
    }

    fun updateList(newList : List<UserNewsUiModel>){
        val diffCallback = GenericDiffUtil(
            oldList = userNewsList,
            newList = newList,
            areItemsSame = { old, new -> old == new },
            areContentsSame = { old, new -> old == new }
        )
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        userNewsList.clear()
        userNewsList.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }
    fun popupMenu(holder: UserNewsViewHolder,news : UserNewsUiModel){
        holder.binding.moreIcon.setOnClickListener {
            NewsPopupHelper(holder.itemView.context,holder.binding.moreIcon, onDeleteClick = { onDeleteClick(news.publishedAt)}).showPopup()
        }

    }


}