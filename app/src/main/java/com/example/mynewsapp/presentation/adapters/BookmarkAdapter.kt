package com.example.mynewsapp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.mynewsapp.databinding.BookmarkItemBinding
import com.example.mynewsapp.presentation.uimodels.common.ArticleUiModel
import com.example.mynewsapp.presentation.uiutils.GenericDiffUtil
import com.example.mynewsapp.presentation.uiutils.NewsPopupHelper


class BookmarkAdapter(val onNewsClick: (ArticleUiModel) -> Unit, val onDeleteClick : (String) -> Unit) :
    RecyclerView.Adapter<BookmarkAdapter.BookmarkViewHolder>() {
    var list = arrayListOf<ArticleUiModel>()

    inner class BookmarkViewHolder(val binding: BookmarkItemBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkViewHolder {
        val binding =
            BookmarkItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookmarkViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int) {
        holder.binding.article = list[position]
        clickEvent(holder, list[position])
    }

    fun updateList(newList : List<ArticleUiModel>){
        val diffCallback = GenericDiffUtil(list,newList, areItemsSame ={old,new -> old.url== new.url}, areContentsSame = {old,new->old == new})
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        list.clear()
        list.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }


    fun clickEvent(holder: BookmarkViewHolder,news : ArticleUiModel) {
        holder.binding.newsItemContainer.setOnClickListener() {
            onNewsClick(news)
        }
            holder.binding.moreIcon.setOnClickListener {
                news.url?.let {NewsPopupHelper(holder.itemView.context,holder.binding.moreIcon, onDeleteClick = {onDeleteClick(it)}).showPopup() }
        }
    }
}