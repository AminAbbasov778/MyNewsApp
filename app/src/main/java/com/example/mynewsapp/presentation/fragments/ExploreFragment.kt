package com.example.mynewsapp.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.mynewsapp.R
import com.example.mynewsapp.presentation.uiutils.VisibilityUtils.setGone
import com.example.mynewsapp.presentation.uiutils.VisibilityUtils.show
import com.example.mynewsapp.databinding.FragmentExploreBinding
import com.example.mynewsapp.presentation.adapters.TopicAdapter
import com.example.mynewsapp.presentation.adapters.TrendingNewsAdapter
import com.example.mynewsapp.presentation.uistates.UiState
import com.example.mynewsapp.presentation.viewmodels.ExploreViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExploreFragment : Fragment() {
  lateinit var binding : FragmentExploreBinding
  val viewModel by viewModels<ExploreViewModel>()
    var trendingNewsAdapter : TrendingNewsAdapter? = null
    var topicAdapter : TopicAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExploreBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpAdapter()
        observe()
        onClick()
    }

    private fun observe(){
        viewModel.trendingNews.observe(viewLifecycleOwner) {
            when(it){
                is UiState.Success -> {
                    binding.popularLoading.setGone()
                    trendingNewsAdapter?.updateList(it.data)
                }
                is UiState.Error ->{
                    binding.popularLoading.setGone()
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
                is UiState.Loading -> binding.popularLoading.show()
            }
        }

        viewModel.topics.observe(viewLifecycleOwner) {
            when(it){
                is UiState.Success -> {
                    binding.topicLoading.setGone()
                    topicAdapter?.updateList(it.data)
                }
                is UiState.Error ->{
                    binding.topicLoading.setGone()
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
                is UiState.Loading -> binding.topicLoading.show()
            }

        }

        viewModel.seeAllStatus.observe(viewLifecycleOwner){
            if(it) binding.topicSeeAllText.setText(R.string.hide)
            else   binding.topicSeeAllText.setText(R.string.see_all)

        }
    }

    private fun setUpAdapter(){
        topicAdapter = TopicAdapter{viewModel.onSaveClick(it)}
        trendingNewsAdapter = TrendingNewsAdapter{
            findNavController().navigate(ExploreFragmentDirections.actionExploreFragmentToDetailFragment(it))
        }
         binding.popularTopicRecView.adapter = trendingNewsAdapter
        binding.topicRecView.adapter = topicAdapter

    }

    private fun onClick(){
        binding.topicSeeAllText.setOnClickListener {
            viewModel.toggleSeeAll()
        }
    }
}