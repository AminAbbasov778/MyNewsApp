package com.example.mynewsapp.presentation.adapters

import android.R.attr.author
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.mynewsapp.R
import com.example.mynewsapp.databinding.AuthorsItemBinding
import com.example.mynewsapp.presentation.uimodels.author.AuthorUiModel
import com.example.mynewsapp.presentation.uimodels.common.FollowUiModel
import com.example.mynewsapp.presentation.uiutils.GenericDiffUtil

class AuthorsAdapter (val onUnFollowClick: (AuthorUiModel) -> Unit) :
    RecyclerView.Adapter<AuthorsAdapter.AuthorsViewHolder>() {

    val followingList = ArrayList<AuthorUiModel>()

    inner class AuthorsViewHolder(val binding: AuthorsItemBinding) : ViewHolder(binding.root)


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): AuthorsViewHolder {
        val binding =
            AuthorsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AuthorsViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: AuthorsViewHolder,
        position: Int,
    ) {
        val list = followingList[position]
        holder.binding.authors = list


        holder.binding.followButton.setOnClickListener {
            onUnFollowClick(list)
        }


    }

    override fun getItemCount(): Int {
        return followingList.size
    }

    fun updateList(newList: List<AuthorUiModel>) {
        val diffCallback = GenericDiffUtil(
            oldList = followingList,
            newList = newList,
            areItemsSame = { old, new -> old.sourceName == new.sourceName },
            areContentsSame = { old, new -> old == new }
        )
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        followingList.clear()
        followingList.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

}