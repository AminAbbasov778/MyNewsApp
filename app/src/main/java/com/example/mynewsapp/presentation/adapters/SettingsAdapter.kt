package com.example.mynewsapp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.mynewsapp.databinding.SettingsItemBinding
import com.example.mynewsapp.presentation.uimodels.settings.SettingsUiModel
import com.example.mynewsapp.presentation.uiutils.GenericDiffUtil
import com.example.mynewsapp.presentation.uiutils.VisibilityUtils.setGone

class SettingsAdapter(val onSettingClick : (SettingsUiModel) -> Unit) : RecyclerView.Adapter<SettingsAdapter.SettingsViewHolder>() {
    var list = arrayListOf<SettingsUiModel>()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): SettingsViewHolder {
        val binding =
            SettingsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SettingsViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: SettingsViewHolder,
        position: Int,
    ) {
        if (position == list.lastIndex) {
            holder.binding.settingsForwardIcon.setGone()
        }
        holder.binding.settings = list[position]
        holder.binding.settingsContainer.setOnClickListener {
            onSettingClick(list[position])
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class SettingsViewHolder(val binding: SettingsItemBinding) : ViewHolder(binding.root)


    fun updateList(newList: List<SettingsUiModel>) {
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