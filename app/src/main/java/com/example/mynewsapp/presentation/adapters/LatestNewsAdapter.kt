package com.example.mynewsapp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mynewsapp.data.model.latestnews.Article
import com.example.mynewsapp.databinding.NewsItemBinding
import com.example.mynewsapp.presentation.uiutils.GenericDiffUtil

class LatestNewsAdapter(val onClickLatestNews: (Article) -> Unit) :
    RecyclerView.Adapter<LatestNewsAdapter.LatestNewsViewHolder>() {

    var latestNews = ArrayList<Article>()

    inner class LatestNewsViewHolder(val binding: NewsItemBinding) :
        RecyclerView.ViewHolder(binding.root)


    fun addItems(newList: List<Article>) {
        val start = latestNews.size
        latestNews.addAll(newList.drop(start))
        notifyItemRangeInserted(start, newList.size - start)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LatestNewsViewHolder {
        val binding = NewsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LatestNewsViewHolder(binding)
    }

    override fun getItemCount() = latestNews.size

    override fun onBindViewHolder(holder: LatestNewsViewHolder, position: Int) {
        holder.binding.latestNews = latestNews[position]
        holder.binding.newsItemContainer.setOnClickListener { onClickLatestNews(latestNews[position]) }
    }

    fun setItems(newList: List<Article>) {
        val diffCallback = GenericDiffUtil(
            oldList = latestNews,
            newList = newList,
            areItemsSame = { old, new -> old == new },
            areContentsSame = { old, new -> old == new }
        )
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        latestNews.clear()
        latestNews.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

}
