package com.example.mynewsapp.presentation.adapters

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mynewsapp.R
import com.example.mynewsapp.databinding.TopicItemBinding
import com.example.mynewsapp.presentation.uimodels.common.TopicUiModel
import com.example.mynewsapp.presentation.uiutils.GenericDiffUtil

class TopicAdapter(val onSaveClick : (TopicUiModel) -> Unit) : RecyclerView.Adapter<TopicAdapter.TopicViewHolder>(){

    val topicList = arrayListOf<TopicUiModel>()
    lateinit var context : Context

    inner class TopicViewHolder(val binding : TopicItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): TopicViewHolder {
       val binding = TopicItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        context = parent.context
        return TopicViewHolder(binding)
    }
    override fun getItemCount(): Int {
        return topicList.size
    }

    override fun onBindViewHolder(
        holder: TopicViewHolder,
        position: Int,
    ) {
        val list = topicList[position]
        holder.binding.topicModel = list
        if (list.isSaved) {
            holder.binding.savedBtn.apply {
               backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context,R.color.primary_blue))
                setTextColor(ContextCompat.getColor(context,R.color.white))
                setText(R.string.saved)
            }

        }else{
            holder.binding.savedBtn.apply {
                backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context,R.color.white))
                setTextColor(ContextCompat.getColor(context,R.color.primary_blue))
                setText(R.string.save)
            }
        }

        holder.binding.savedBtn.setOnClickListener() {
            onSaveClick(list)
        }

    }




    fun updateList(newList : List<TopicUiModel>){
        val diffCallback = GenericDiffUtil(
            oldList = topicList,
            newList = newList,
            areItemsSame = { old, new -> old.topic == new.topic },
            areContentsSame = { old, new -> old == new }
        )
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        topicList.clear()
        topicList.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }


}