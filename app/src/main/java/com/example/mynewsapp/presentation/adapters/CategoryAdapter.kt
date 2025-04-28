package com.example.mynewsapp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mynewsapp.databinding.CategoryItemBinding
import com.example.mynewsapp.presentation.uiutils.VisibilityUtils.setGone
import com.example.mynewsapp.presentation.uiutils.VisibilityUtils.show
import com.example.mynewsapp.presentation.uiutils.GenericDiffUtil

class CategoryAdapter(
    private val onClickCategory: (String) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    private val categoryList = arrayListOf<String>()
    private var selectedPosition: Int = 0

    inner class CategoryViewHolder( val binding: CategoryItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = CategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        bind(holder.binding,categoryList[position], position)
    }

    override fun getItemCount(): Int = categoryList.size

    fun updateList(newList: ArrayList<String>) {
        val diffCallback = GenericDiffUtil(
            oldList = categoryList,
            newList = newList,
            areItemsSame = { old, new -> old == new },
            areContentsSame = { old, new -> old == new }
        )
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        categoryList.clear()
        categoryList.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    fun bind(binding: CategoryItemBinding,category: String, position: Int) {
        binding.category = category


        if (position == selectedPosition) {
            binding.divider.show()
        } else {
            binding.divider.setGone()
        }


        binding.categoryName.setOnClickListener {
            if (selectedPosition == position) return@setOnClickListener

            val previousSelected = selectedPosition
            selectedPosition = position
            onClickCategory(category)

            if (previousSelected != -1) notifyItemChanged(previousSelected)
            notifyItemChanged(selectedPosition)
        }
    }
}
