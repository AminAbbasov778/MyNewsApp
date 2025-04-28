package com.example.mynewsapp.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.mynewsapp.R
import com.example.mynewsapp.databinding.FragmentFollowingBinding
import com.example.mynewsapp.presentation.adapters.FollowAdapter
import com.example.mynewsapp.presentation.uistates.UiState
import com.example.mynewsapp.presentation.uiutils.VisibilityUtils.setGone
import com.example.mynewsapp.presentation.uiutils.VisibilityUtils.show
import com.example.mynewsapp.presentation.viewmodels.FollowingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FollowingFragment : Fragment() {
    lateinit var binding : FragmentFollowingBinding
    val viewModel by viewModels<FollowingViewModel>()
    var followAdapter : FollowAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowingBinding.inflate(inflater,container,false)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter()
        observe()
    }

    private fun setupAdapter(){
        followAdapter = FollowAdapter{viewModel.unfollowNewsSource(it)}
        binding.followingsRecView.adapter =followAdapter
    }
    private fun observe(){
        viewModel.followedSources.observe(viewLifecycleOwner){
            when(it){
                is UiState.Success ->{
                    binding.loading.setGone()
                    followAdapter?.updateList(it.data)

                }
                is UiState.Error ->{
                    binding.loading.setGone()
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
                is UiState.Loading -> binding.loading.show()
            }
        }
        viewModel.followState.observe(viewLifecycleOwner){
            when(it){
                is UiState.Success -> binding.loading.setGone()
                is UiState.Error -> {
                    binding.loading.setGone()
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
                is UiState.Loading -> binding.loading.show()
            }
        }
    }
}