package com.example.mynewsapp.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mynewsapp.presentation.uiutils.VisibilityUtils.setGone
import com.example.mynewsapp.presentation.uiutils.VisibilityUtils.show
import com.example.mynewsapp.databinding.FragmentSearchBinding
import com.example.mynewsapp.presentation.adapters.LatestNewsAdapter
import com.example.mynewsapp.presentation.uistates.UiState
import com.example.mynewsapp.presentation.viewmodels.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private val viewModel: SearchViewModel by viewModels()
    private lateinit var newsAdapter: LatestNewsAdapter
    private var recyclerViewState: Parcelable? = null
    private var searchJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupEditText()
        setupSearch()
        setupNewsAdapter()
        observeData()
        setupScrollListener()
        recyclerViewState?.let {
            binding.searchedNewsRecView.layoutManager?.onRestoreInstanceState(it)
        }
    }

    private fun setupEditText() {
        binding.searchEditText.post {
            binding.searchEditText.requestFocus()
            val inputMethodManager =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(
                binding.searchEditText,
                InputMethodManager.SHOW_IMPLICIT
            )
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

    private fun setupNewsAdapter() {
        newsAdapter = LatestNewsAdapter {
            recyclerViewState = binding.searchedNewsRecView.layoutManager?.onSaveInstanceState()
            findNavController().navigate(
                SearchFragmentDirections.actionSearchFragmentToDetailFragment(
                    it
                )
            )
        }
        binding.searchedNewsRecView.adapter = newsAdapter
    }


    private fun observeData() {
        viewModel.searchedNews.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Success -> {
                    binding.loading.setGone()
                    newsAdapter.setItems(state.data)
                    recyclerViewState?.let {
                        binding.searchedNewsRecView.layoutManager?.onRestoreInstanceState(it)

                    }
                }

                is UiState.Error -> {
                    binding.loading.setGone()
                    binding.searchedNewsRecView.setGone()
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                }

                is UiState.Loading -> binding.loading.show()
            }
        }


    }

    private fun setupScrollListener() {
        binding.searchedNewsRecView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount

                if (!recyclerView.canScrollVertically(1) && firstVisibleItemPosition + visibleItemCount >= totalItemCount - 5) {
                    viewModel.loadMoreNews()
                }
            }
        })
    }


}