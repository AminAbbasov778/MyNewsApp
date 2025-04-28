package com.example.mynewsapp.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.mynewsapp.presentation.uiutils.VisibilityUtils.setGone
import com.example.mynewsapp.presentation.uiutils.VisibilityUtils.show
import com.example.mynewsapp.databinding.FragmentTrendingBinding
import com.example.mynewsapp.presentation.adapters.TrendingNewsAdapter
import com.example.mynewsapp.presentation.uistates.UiState
import com.example.mynewsapp.presentation.viewmodels.TrendingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TrendingFragment : Fragment() {
    lateinit var binding: FragmentTrendingBinding
    lateinit var adapter: TrendingNewsAdapter
    val viewModel: TrendingViewModel by viewModels<TrendingViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentTrendingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        provideAdapter()
        observe()
        onClick()
    }

    fun provideAdapter() {
        adapter = TrendingNewsAdapter {
            findNavController().navigate(
                TrendingFragmentDirections.actionTrendingFragmentToDetailFragment(
                    it
                )
            )
        }
        binding.trendingNewsRecView.adapter = adapter
    }

    fun observe() {
        viewModel.trendingNews.observe(viewLifecycleOwner) { state ->
            when (state) {

                is UiState.Success -> {
                    binding.loading.setGone()
                    adapter.updateList(state.data)
                }

                is UiState.Error -> {
                    binding.loading.setGone()
                    Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                }
                else -> binding.loading.show()

            }

        }
    }

    fun onClick() {
        binding.backIcon.setOnClickListener {
            findNavController().popBackStack()
        }
    }

}