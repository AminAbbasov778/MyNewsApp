package com.example.mynewsapp.domain.domainmodels

data class CommentDomainModel(
    val comment: String,
    val profileImg: String,
    val username: String,
    val userId: String,
    val commentedAt: String,
    val timeDifference: String,
    val url: String,
    val isReply: Boolean,
    val parentCommentId: String?,
    val parentUsername : String?,
    val likesCount: Int = 0,
    val isLiked: Boolean = false,
    val replies: List<CommentDomainModel> = emptyList(),

)