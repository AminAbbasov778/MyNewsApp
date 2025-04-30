package com.example.mynewsapp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mynewsapp.databinding.ReplyItemBinding
import com.example.mynewsapp.presentation.uimodels.comment.CommentsUiModel
import com.example.mynewsapp.presentation.uiutils.GenericDiffUtil
import com.example.mynewsapp.presentation.uiutils.VisibilityUtils.setGone
import com.example.mynewsapp.presentation.uiutils.VisibilityUtils.show

class ReplyAdapter(
    private val onReplyClick: (CommentsUiModel) -> Unit,
    private val onFavoriteClick: (CommentsUiModel) -> Unit,
) : RecyclerView.Adapter<ReplyAdapter.ReplyViewHolder>() {

    private val replyList = ArrayList<CommentsUiModel>()

    inner class ReplyViewHolder(val binding: ReplyItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReplyViewHolder {
        val binding = ReplyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReplyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReplyViewHolder, position: Int) {
        val list = replyList[position]
        holder.binding.reply = list
        if (list.isLiked) {
            holder.binding.favoriteIcon.show()
            holder.binding.unFavoriteIcon.setGone()
        } else {
            holder.binding.unFavoriteIcon.show()
            holder.binding.favoriteIcon.setGone()
        }
        holder.binding.replyUsernameTv.apply {
            show()
            text = "@${list.parentUsername}"
        }
        holder.binding.replyContainer.setOnClickListener {
            onReplyClick(list)
        }
        holder.binding.favoriteIcon.setOnClickListener {
            onFavoriteClick(list)
        }
        holder.binding.unFavoriteIcon.setOnClickListener {
            onFavoriteClick(list)
        }
    }

    override fun getItemCount(): Int = replyList.size

    fun updateList(newList: List<CommentsUiModel>) {
        val diffCallback = GenericDiffUtil(
            oldList = replyList,
            newList = newList,
            areItemsSame = { old, new -> old.commentedAt == new.commentedAt },
            areContentsSame = { old, new -> old == new }
        )
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        replyList.clear()
        replyList.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }
}