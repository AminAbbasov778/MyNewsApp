package com.example.mynewsapp.domain.domainmodels

data class CommentModel(
    val comment: String,
    val profileImg: String,
    val username: String,
    val userId: String,
    val commentedAt: String,
    val timeDifference: String= "Empty difference",
    val url: String,
    val isReply: Boolean,
    val parentCommentId: String?,
    val parentUsername : String?,
    val likesCount: Int = 0,
    val isLiked: Boolean = false,
    val replies: List<CommentModel> = emptyList(),

    )