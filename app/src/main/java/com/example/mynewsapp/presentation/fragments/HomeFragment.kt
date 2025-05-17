package com.example.mynewsapp.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mynewsapp.R
import com.example.mynewsapp.databinding.FragmentHomeBinding
import com.example.mynewsapp.presentation.adapters.CategoryAdapter
import com.example.mynewsapp.presentation.adapters.LatestNewsAdapter
import com.example.mynewsapp.presentation.uistates.UiState
import com.example.mynewsapp.presentation.uiutils.PicassoUtil.LoadUrl
import com.example.mynewsapp.presentation.uiutils.VisibilityUtils.setGone
import com.example.mynewsapp.presentation.uiutils.VisibilityUtils.show
import com.example.mynewsapp.presentation.viewmodels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    val viewModel by viewModels<HomeViewModel>()
    private lateinit var latestNewsAdapter: LatestNewsAdapter
    lateinit var categoryAdapter: CategoryAdapter
    private var firstVisiblePosition: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        observe()
        onCLick()
    }



    private fun setupUI() {
        latestNewsAdapter = LatestNewsAdapter (
            onNewsClick = {
                val layoutManager = binding.latestNewsRecView.layoutManager as LinearLayoutManager
                firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToDetailFragment(
                        it
                    )
                )
            },

        )
        binding.latestNewsRecView.adapter = latestNewsAdapter
        binding.latestNewsRecView.layoutManager = LinearLayoutManager(context)
        setupScrollListener()

        categoryAdapter = CategoryAdapter {
            viewModel.getLatestNewsResult(category = getString(it))
        }
        binding.newsCategoriesRecView.adapter = categoryAdapter
    }

    private fun observe() {
        viewModel.latestNewsState.observe(viewLifecycleOwner) {
            when (it) {
                UiState.Loading -> binding.latestNewsloading.show()
                is UiState.Success -> {
                    binding.latestNewsloading.setGone()
                    val layoutManager =
                        binding.latestNewsRecView.layoutManager as LinearLayoutManager
                    val position = layoutManager.findFirstVisibleItemPosition()
                    if (position == 0 || it.data.size <= 10) {
                        latestNewsAdapter.setItems(it.data)
                    } else {
                        latestNewsAdapter.addItems(it.data)
                        if (firstVisiblePosition > 0) layoutManager.scrollToPosition(
                            firstVisiblePosition
                        )
                    }
                }

                is UiState.Error -> {
                    binding.latestNewsloading.setGone()
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.trendingNewsState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Success -> {
                    if (it.data.isNotEmpty()) {
                        binding.trendNewsTitle.text = it.data[0].title
                        binding.trendNewsTimeDifferenceText.text = it.data[0].timeDifference
                        binding.authorName.text = it.data[0].source?.name
                        binding.trendNewsImage.LoadUrl(it.data[0].urlToImage)

                    }
                    binding.trendingLoading.setGone()
                }

                is UiState.Error -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    binding.trendingLoading.setGone()
                }

                else -> binding.trendingLoading.show()
            }
        }


        viewModel.categoryList.observe(viewLifecycleOwner) { categories ->
            categoryAdapter.updateList(categories)
        }

        viewModel.isSeeAllActive.observe(viewLifecycleOwner){
            if(it){
                binding.trendingNewsHeaderConstraint.setGone()
                binding.latestNewsSeeAllText.setText(R.string.hide)
                firstVisiblePosition = 0
            }else{
                binding.trendingNewsHeaderConstraint.show()
               binding.latestNewsSeeAllText.setText(R.string.see_all)
                firstVisiblePosition = 0
            }
        }
    }

    private fun onCLick() {
        binding.latestNewsSeeAllText.setOnClickListener {
           viewModel.toggleSeeAll()

        }


        binding.trendingNewsBodyConstraint.setOnClickListener {
            viewModel.trendingNewsState.value?.let { state ->
                if (state is UiState.Success && state.data.isNotEmpty()) {
                    findNavController().navigate(
                        HomeFragmentDirections.actionHomeFragmentToDetailFragment2(
                            state.data[0]
                        )
                    )
                } else {
                    Toast.makeText(context,R.string.empty_trending, Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.searchEditText.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSearchFragment())
        }


        binding.trendingNewsSeeAllText.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToTrendingFragment())
        }

    }

    private fun setupScrollListener() {
        binding.latestNewsRecView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastVisiblePosition = layoutManager.findLastVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount
                if (!recyclerView.canScrollVertically(1) && lastVisiblePosition >= totalItemCount - 2) {
                    viewModel.loadMoreNews()
                }
            }
        })
    }
}