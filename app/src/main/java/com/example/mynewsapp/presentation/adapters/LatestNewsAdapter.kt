package com.example.mynewsapp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mynewsapp.databinding.NewsItemBinding
import com.example.mynewsapp.presentation.uimodels.common.ArticleUiModel
import com.example.mynewsapp.presentation.uiutils.GenericDiffUtil

class LatestNewsAdapter(val onNewsClick: (ArticleUiModel) -> Unit,) :
    RecyclerView.Adapter<LatestNewsAdapter.LatestNewsViewHolder>() {

    var news = ArrayList<ArticleUiModel>()

    inner class LatestNewsViewHolder(val binding: NewsItemBinding) :
        RecyclerView.ViewHolder(binding.root)


    fun addItems(newList: List<ArticleUiModel>) {
        val start = news.size
        news.addAll(newList.drop(start))
        notifyItemRangeInserted(start, newList.size - start)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LatestNewsViewHolder {
        val binding = NewsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LatestNewsViewHolder(binding)
    }

    override fun getItemCount() = news.size

    override fun onBindViewHolder(holder: LatestNewsViewHolder, position: Int) {
        holder.binding.latestNews = news[position]
        clickEvent(holder,news[position])
    }

    fun clickEvent(holder: LatestNewsViewHolder,news : ArticleUiModel) {
        holder.binding.newsItemContainer.setOnClickListener() {
            onNewsClick(news)
        }
    }

    fun setItems(newList: List<ArticleUiModel>) {
        val diffCallback = GenericDiffUtil(
            oldList = news,
            newList = newList,
            areItemsSame = { old, new -> old.url == new.url },
            areContentsSame = { old, new -> old == new }
        )
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        news.clear()
        news.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

}
