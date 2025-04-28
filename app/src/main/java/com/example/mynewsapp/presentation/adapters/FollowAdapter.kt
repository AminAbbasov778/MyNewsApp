package com.example.mynewsapp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.mynewsapp.databinding.FollowingItemBinding
import com.example.mynewsapp.presentation.uimodels.common.FollowUiModel
import com.example.mynewsapp.presentation.uiutils.GenericDiffUtil

class FollowAdapter(val onUnFollowClick : (String) -> Unit) : RecyclerView.Adapter<FollowAdapter.FollowViewHolder>()  {

      val followingList = ArrayList<FollowUiModel>()

    inner class FollowViewHolder(val binding : FollowingItemBinding) : ViewHolder(binding.root)


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): FollowViewHolder {
       val binding = FollowingItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return FollowViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: FollowViewHolder,
        position: Int,
    ) {
      val list =  followingList[position]
      holder.binding.following = list
        holder.binding.followButton.setOnClickListener {
            onUnFollowClick(list.sourceName)
        }
    }

    override fun getItemCount(): Int {
        return followingList.size
    }

fun updateList(newList : List<FollowUiModel>){
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