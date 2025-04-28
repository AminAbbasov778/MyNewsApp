package com.example.mynewsapp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.mynewsapp.data.model.latestnews.Article
import com.example.mynewsapp.databinding.TrendingItemBinding
import com.example.mynewsapp.presentation.uiutils.GenericDiffUtil

class TrendingNewsAdapter(val clickOnNews: (Article) -> Unit) :
    RecyclerView.Adapter<TrendingNewsAdapter.TrendingNewsViewHolder>() {

    var list = arrayListOf<Article>()

    inner class TrendingNewsViewHolder(val binding: TrendingItemBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrendingNewsViewHolder {
        var binding =
            TrendingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrendingNewsViewHolder(binding)
    }

    override fun getItemCount(): Int {

        return list.size
    }

    override fun onBindViewHolder(holder: TrendingNewsViewHolder, position: Int) {
        holder.binding.trendingnews = list[position]

        holder.binding.main.setOnClickListener() {
            clickOnNews(list[position])
        }
    }


    fun updateList(newList: List<Article>) {
        val diffCallback = GenericDiffUtil(
            oldList = list,
            newList = newList,
            areItemsSame = { old, new -> old == new },
            areContentsSame = { old, new -> old == new }
        )
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        list.clear()
        list.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }


}