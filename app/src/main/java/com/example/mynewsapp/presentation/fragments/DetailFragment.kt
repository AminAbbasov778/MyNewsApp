package com.example.mynewsapp.presentation.fragments

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.mynewsapp.R
import com.example.mynewsapp.Utils.NewsSourceConstants
import com.example.mynewsapp.databinding.FragmentDetailBinding
import com.example.mynewsapp.presentation.uistates.UiState
import com.example.mynewsapp.presentation.uiutils.PicassoUtil.LoadUrl
import com.example.mynewsapp.presentation.uiutils.VisibilityUtils.setGone
import com.example.mynewsapp.presentation.uiutils.VisibilityUtils.show
import com.example.mynewsapp.presentation.viewmodels.DetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {
    private lateinit var binding: FragmentDetailBinding
    private val args: DetailFragmentArgs by navArgs()
    private val viewModel by viewModels<DetailViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.resetState()
        provideUIWithData()
        onClickEvents()
        initialFun()
        observe()
    }

    private fun provideUIWithData() {
        binding.newsImage.LoadUrl(args.article.urlToImage)
        binding.newsTitle.text = args.article.title
        binding.newsDescription.text = args.article.description
        binding.publisherCompanyNameText.text = args.article.source?.name
        binding.publishTimeText.text = args.article.timeDifference
        binding.publisherLogo.LoadUrl(NewsSourceConstants.sourceLogo)
    }

    private fun onClickEvents() {
        binding.unselectedBookmarkIcon.setOnClickListener {
            viewModel.toggleBookmark(args.article)
        }
        binding.selectedBookmarkIcon.setOnClickListener {
            viewModel.toggleBookmark(args.article)
        }
        binding.backIcon.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.followButton.setOnClickListener {
            args.article.source?.name?.let {
                viewModel.toggleFollowingBtn(it, NewsSourceConstants.sourceLogo, 125000)
            }
        }
        binding.favoriteIcon.setOnClickListener {
            args.article.url?.let {
                Log.d("DetailFragment", "Favorite icon clicked for URL: $it")
                viewModel.toggleFavorite(it)
            }
        }
        binding.unFavoriteIcon.setOnClickListener {
            args.article.url?.let {
                Log.d("DetailFragment", "Unfavorite icon clicked for URL: $it")
                viewModel.toggleFavorite(it)
            }
        }
        binding.commentIcon.setOnClickListener {
            args.article.url?.let {
                findNavController().navigate(
                    DetailFragmentDirections.actionDetailFragmentToCommentFragment(it)
                )
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun observe() {
        viewModel.actionState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Success -> binding.loading.setGone()
                is UiState.Error -> {
                    binding.loading.setGone()
                    Toast.makeText(context, getString(state.message), Toast.LENGTH_SHORT).show()
                }
                is UiState.Loading -> binding.loading.show()
            }
        }

        viewModel.isBookmarked.observe(viewLifecycleOwner) { isBookmarked ->
            if (isBookmarked) {
                binding.unselectedBookmarkIcon.setGone()
                binding.selectedBookmarkIcon.show()
            } else {
                binding.unselectedBookmarkIcon.show()
                binding.selectedBookmarkIcon.setGone()
            }
        }

        viewModel.isFollowing.observe(viewLifecycleOwner) { isFollowing ->
            if (isFollowing) {
                binding.followButton.apply {
                    backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.primary_blue))
                    setText(R.string.following)
                    setTextColor(ContextCompat.getColor(context, R.color.btnColor))
                    strokeWidth = 0
                }
            } else {
                binding.followButton.apply {
                    backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white))
                    setText(R.string.follow)
                    setTextColor(ContextCompat.getColor(context, R.color.primary_blue))
                    setStrokeColorResource(R.color.primary_blue)
                    strokeWidth = 7
                }
            }
        }

        viewModel.isFavorite.observe(viewLifecycleOwner) { isFavorite ->
            Log.d("DetailFragment", "isFavorite changed: $isFavorite")
            if (isFavorite) {
                binding.favoriteIcon.show()
                binding.unFavoriteIcon.setGone()
            } else {
                binding.unFavoriteIcon.show()
                binding.favoriteIcon.setGone()
            }
        }

        viewModel.favoriteCount.observe(viewLifecycleOwner) { state ->
            Log.d("DetailFragment", "favoriteCount changed: $state")
            when (state) {
                is UiState.Success -> {
                    binding.loading.setGone()
                    binding.favoriteCount.text = state.data.toString()
                    if (state.data == 0 && viewModel.isFavorite.value == true) {
                        Log.d("DetailFragment", "favoriteCount is 0, resetting isFavorite")
                        viewModel.favoriteStatus()
                    }
                }
                is UiState.Error -> {
                    binding.loading.setGone()
                    Toast.makeText(context, getString(state.message), Toast.LENGTH_SHORT).show()
                }
                is UiState.Loading -> binding.loading.show()
            }
        }

        viewModel.commentCount.observe(viewLifecycleOwner) { state ->
            Log.d("DetailFragment", "commentCount changed: $state")
            when (state) {
                is UiState.Success -> {
                    binding.loading.setGone()
                    binding.commentCount.text = state.data.toString()
                }
                is UiState.Error -> {
                    binding.loading.setGone()
                    Toast.makeText(context, getString(state.message), Toast.LENGTH_SHORT).show()
                }
                is UiState.Loading -> binding.loading.show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initialFun() {
        viewModel.getIsNewsBookmarked(args.article)
        args.article.source?.name?.let { viewModel.isNewsSourceFollowed(it) }
        args.article.url?.let {
            viewModel.getComments(it)
            viewModel.getFavoriteStatus(it)
        }
    }
}