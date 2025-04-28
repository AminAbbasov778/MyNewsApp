package com.example.mynewsapp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.mynewsapp.databinding.BoardingItemBinding
import com.example.mynewsapp.presentation.uimodels.boarding.BoardingModel
import com.example.mynewsapp.presentation.uiutils.GenericDiffUtil

class OnBoardingAdapter() : RecyclerView.Adapter<OnBoardingAdapter.OnBoardingViewHolder>() {


    inner class OnBoardingViewHolder(val binding: BoardingItemBinding) : ViewHolder(binding.root)

    var boardingModelList = ArrayList<BoardingModel>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnBoardingViewHolder {
        val binding =
            BoardingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OnBoardingViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return boardingModelList.size
    }

    override fun onBindViewHolder(holder: OnBoardingViewHolder, position: Int) {
        holder.binding.boarding = boardingModelList[position]
    }


    fun updateList(newBoardingModelList: List<BoardingModel>) {
        val diffCallback = GenericDiffUtil(
            oldList = boardingModelList,
            newList = newBoardingModelList,
            areItemsSame = { old, new -> old == new },
            areContentsSame = { old, new -> old == new }
        )
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        boardingModelList.clear()
        boardingModelList.addAll(newBoardingModelList)
        diffResult.dispatchUpdatesTo(this)
    }


}