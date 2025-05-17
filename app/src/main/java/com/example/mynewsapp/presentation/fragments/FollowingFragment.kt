package com.example.mynewsapp.presentation.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.mynewsapp.databinding.FragmentFollowingBinding
import com.example.mynewsapp.presentation.adapters.FollowAdapter
import com.example.mynewsapp.presentation.uistates.UiState
import com.example.mynewsapp.presentation.uiutils.VisibilityUtils.setGone
import com.example.mynewsapp.presentation.uiutils.VisibilityUtils.show
import com.example.mynewsapp.presentation.viewmodels.FollowingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FollowingFragment : Fragment() {
    lateinit var binding : FragmentFollowingBinding
    val viewModel by viewModels<FollowingViewModel>()
    var followAdapter : FollowAdapter? = null
    private var searchJob: Job? = null

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
        setupSearch()
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

        viewModel.searchedSources.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Success -> {
                    binding.loading.setGone()
                    followAdapter?.updateList(state.data)
                }

                is UiState.Error -> {
                    binding.loading.setGone()
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                }
                is UiState.Loading -> binding.loading.show()
            }
        }

    }

    private fun setupSearch() {
        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                searchJob?.cancel()
                val query = s.toString()
                searchJob = viewLifecycleOwner.lifecycleScope.launch {
                    delay(300)
                    viewModel.searchNews(query)
                }
            }
        })
    }
}