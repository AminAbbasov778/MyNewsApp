package com.example.mynewsapp.presentation.uimodels.comment

import android.graphics.Bitmap

data class CommentUiModel(
    val comment: String,
    val profileImg: Bitmap?,
    val username: String,
    val userId: String,
    val commentedAt: String,
    val timeDifference: String,
    val url: String,
    val isReply: Boolean,
    val parentCommentId: String?,
    val parentUsername: String?,
    val likesCount: Int = 0,
    val isLiked: Boolean = false,
    val replies: List<CommentUiModel> = emptyList(),
) {
}