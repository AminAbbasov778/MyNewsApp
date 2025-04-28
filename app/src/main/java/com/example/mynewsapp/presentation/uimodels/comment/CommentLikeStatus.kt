package com.example.mynewsapp.presentation.uimodels.comment

data class CommentLikeStatus(
    val commentId: String,
    val isLiked: Boolean,
    val likesCount: Int
)