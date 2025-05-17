package com.example.mynewsapp.presentation.uimodels.comment

import android.graphics.Bitmap

data class CommentsUiModel(
    val comment: String,
    val profileImg: Bitmap?,
    val username: String,
    val userId: String,
    val commentedAt: String,
    val timeDifference: String?,
    val url: String,
    val isReply: Boolean,
    val parentCommentId: String?,
    val parentUsername: String?,
    val likesCount: Int = 0,
    val replies: List<CommentsUiModel> = emptyList(),
    val isLiked: Boolean = false,
) {
}