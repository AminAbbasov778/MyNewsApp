package com.example.mynewsapp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.mynewsapp.databinding.BoardingItemBinding
import com.example.mynewsapp.presentation.uimodels.boarding.BoardingUiModel
import com.example.mynewsapp.presentation.uiutils.GenericDiffUtil

class OnBoardingAdapter() : RecyclerView.Adapter<OnBoardingAdapter.OnBoardingViewHolder>() {


    inner class OnBoardingViewHolder(val binding: BoardingItemBinding) : ViewHolder(binding.root)

    var boardingUiModelList = ArrayList<BoardingUiModel>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnBoardingViewHolder {
        val binding =
            BoardingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OnBoardingViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return boardingUiModelList.size
    }

    override fun onBindViewHolder(holder: OnBoardingViewHolder, position: Int) {
        holder.binding.boarding = boardingUiModelList[position]
    }


    fun updateList(newBoardingUiModelList: List<BoardingUiModel>) {
        val diffCallback = GenericDiffUtil(
            oldList = boardingUiModelList,
            newList = newBoardingUiModelList,
            areItemsSame = { old, new -> old == new },
            areContentsSame = { old, new -> old == new }
        )
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        boardingUiModelList.clear()
        boardingUiModelList.addAll(newBoardingUiModelList)
        diffResult.dispatchUpdatesTo(this)
    }


}