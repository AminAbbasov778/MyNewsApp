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
import androidx.navigation.fragment.findNavController
import com.example.mynewsapp.databinding.FragmentBookmarkBinding
import com.example.mynewsapp.presentation.adapters.BookmarkAdapter
import com.example.mynewsapp.presentation.uistates.UiState
import com.example.mynewsapp.presentation.uiutils.VisibilityUtils.setGone
import com.example.mynewsapp.presentation.uiutils.VisibilityUtils.show
import com.example.mynewsapp.presentation.viewmodels.BookmarkViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BookmarkFragment : Fragment() {
    private lateinit var binding: FragmentBookmarkBinding
    val viewModel by viewModels<BookmarkViewModel>()
    lateinit var adapter: BookmarkAdapter
    private var searchJob: Job? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentBookmarkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        provideNewsAdapter()
        observe()
        setupSearch()

    }

    fun provideNewsAdapter() {
        adapter = BookmarkAdapter(
            onNewsClick = {
                findNavController().navigate(
                    BookmarkFragmentDirections.actionBookmarkFragmentToDetailFragment(
                       it
                    )
                ) },
            onDeleteClick = {
                viewModel.deleteBookmark(it)}
        )
        binding.bookmarkRecView.adapter = adapter
    }

    fun observe() {
        viewModel.bookmarkState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.loading.show()
                }

                is UiState.Success -> {
                    binding.loading.setGone()
                    adapter.updateList(state.data)
                }

                is UiState.Error -> {
                    binding.loading.setGone()
                    Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                }
            }
        }



        viewModel.isProductDeleted.observe(viewLifecycleOwner) {
            when(it){
                is UiState.Success -> binding.loading.setGone()
                    is UiState.Error -> {
                        binding.loading.setGone()
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    }
                is UiState.Loading -> binding.loading.show()


            }
        }

        viewModel.searchedNews.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Success -> {
                    binding.loading.setGone()
                    adapter.updateList(state.data)
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