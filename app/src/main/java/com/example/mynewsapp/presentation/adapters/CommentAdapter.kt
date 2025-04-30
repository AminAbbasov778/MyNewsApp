package com.example.mynewsapp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mynewsapp.databinding.CommentItemBinding
import com.example.mynewsapp.presentation.uimodels.comment.CommentsUiModel
import com.example.mynewsapp.presentation.uiutils.GenericDiffUtil
import com.example.mynewsapp.presentation.uiutils.VisibilityUtils.setGone
import com.example.mynewsapp.presentation.uiutils.VisibilityUtils.show

class CommentAdapter(
    private val onReplyClick: (CommentsUiModel) -> Unit,
    private val onLikeClick: (CommentsUiModel) -> Unit
) : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

     val commentList = ArrayList<CommentsUiModel>()

    inner class CommentViewHolder(
        private val binding: CommentItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val replyAdapter = ReplyAdapter(onReplyClick = {onReplyClick(it)}, onFavoriteClick = { onLikeClick(it)})

        init {
            binding.replyRecView.adapter = replyAdapter
            binding.replyContainer.setOnClickListener {
                onReplyClick(commentList[adapterPosition])
            }
            binding.favoriteIcon.setOnClickListener {
                onLikeClick(commentList[adapterPosition])
            }
            binding.unFavoriteIcon.setOnClickListener {
                onLikeClick(commentList[adapterPosition])
            }
        }

        fun bind(comment: CommentsUiModel) {
            binding.comments = comment
            replyAdapter.updateList(comment.replies)
            if(comment.isLiked){
                binding.favoriteIcon.show()
                binding.unFavoriteIcon.setGone()
            }else{
                binding.unFavoriteIcon.show()
                binding.favoriteIcon.setGone()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val binding = CommentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.bind( commentList[position])
    }

    override fun getItemCount(): Int = commentList.size

    fun updateList(newList: List<CommentsUiModel>) {
        val diffCallback = GenericDiffUtil(
            oldList = commentList,
            newList = newList,
            areItemsSame = { old, new -> old.commentedAt == new.commentedAt },
            areContentsSame = { old, new -> old == new }
        )
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        commentList.clear()
        commentList.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }
}