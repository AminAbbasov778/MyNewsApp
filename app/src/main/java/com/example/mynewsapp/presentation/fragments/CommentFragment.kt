package com.example.mynewsapp.presentation.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.mynewsapp.databinding.FragmentCommentBinding
import com.example.mynewsapp.presentation.adapters.CommentAdapter
import com.example.mynewsapp.presentation.uistates.UiState
import com.example.mynewsapp.presentation.uiutils.VisibilityUtils.setGone
import com.example.mynewsapp.presentation.uiutils.VisibilityUtils.show
import com.example.mynewsapp.presentation.viewmodels.CommentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommentFragment : Fragment() {
    private lateinit var binding: FragmentCommentBinding
    private val viewModel by viewModels<CommentViewModel>()
    private val args: CommentFragmentArgs by navArgs()
    private var commentAdapter: CommentAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentCommentBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getComments(args.url)
        setupAdapter()
        observe()
        onClick()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun onClick() {
        binding.sendIconCard.setOnClickListener {
            val comment = binding.commentEditText.text.toString()
            viewModel.addComment(comment, args.url)
        }
        binding.removeIcon.setOnClickListener {
            viewModel.clearData()
        }
        binding.backIcon.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun observe() {
        viewModel.commentState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Success -> {
                    binding.loading.setGone()
                    binding.commentEditText.text.clear()
                }
                is UiState.Error -> {
                    binding.loading.setGone()
                    Toast.makeText(context, getString(it.message), Toast.LENGTH_SHORT).show()
                }
                is UiState.Loading -> binding.loading.show()
            }
        }
        viewModel.comments.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Success -> {
                    binding.loading.setGone()
                    commentAdapter?.updateList(it.data)
                }
                is UiState.Error -> {
                    binding.loading.setGone()
                    Toast.makeText(context, getString(it.message), Toast.LENGTH_SHORT).show()
                }
                is UiState.Loading -> binding.loading.show()
            }
        }
        viewModel.selectedReplyComment.observe(viewLifecycleOwner) {
            it?.let {
                binding.replyCardView.show()
                binding.repliedReviewerUsername.text = "@${it.username}"
            } ?: binding.replyCardView.setGone()
        }
        viewModel.likeState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Success -> binding.loading.setGone()
                is UiState.Error -> {
                    binding.loading.setGone()
                    Toast.makeText(context, getString(it.message), Toast.LENGTH_SHORT).show()
                }
                is UiState.Loading -> binding.loading.show()
            }
        }
    }

    private fun setupAdapter() {
        commentAdapter = CommentAdapter(
            onReplyClick = {
                viewModel.setReplyComment(it)
                binding.commentEditText.requestFocus()
            },
            onLikeClick = { comment ->
                viewModel.toggleLikeStatus(comment.commentedAt, args.url, comment.isLiked)
            }
        )
        binding.commentsRecView.adapter = commentAdapter
    }
}
