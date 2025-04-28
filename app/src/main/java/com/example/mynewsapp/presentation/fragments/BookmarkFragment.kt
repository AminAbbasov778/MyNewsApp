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
import com.example.mynewsapp.databinding.FragmentBookmarkBinding
import com.example.mynewsapp.presentation.adapters.BookmarkAdapter
import com.example.mynewsapp.presentation.uistates.UiState
import com.example.mynewsapp.presentation.viewmodels.BookmarkViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookmarkFragment : Fragment() {
    private lateinit var binding: FragmentBookmarkBinding
    val viewModel by viewModels<BookmarkViewModel>()
    lateinit var adapter: BookmarkAdapter

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

    }

    fun provideNewsAdapter() {
        adapter = BookmarkAdapter(
            onNewsClick = {
                viewModel.convertBookmarkEntityToArticle(it) },
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

        viewModel.newsArticle.observe(viewLifecycleOwner) { navState ->
            if (navState.state == true && navState.article != null) {
                findNavController().navigate(
                    BookmarkFragmentDirections.actionBookmarkFragmentToDetailFragment(
                        navState.article!!
                    )
                )
                viewModel.clearArticle()
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
    }
}