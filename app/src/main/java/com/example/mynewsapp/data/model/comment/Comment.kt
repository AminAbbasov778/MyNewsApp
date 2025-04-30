package com.example.mynewsapp.data.model.comment


data class Comment(
    val comment: String = "",
    val profileImg: String = "",
    val username: String = "",
    val userId: String = "",
    val commentedAt: String = "",
    val url: String = "",
    val isReply: Boolean = false ,
    val parentCommentId: String? = null,
    val parentUsername : String? = null,
    val likesCount: Int = 0,
    val isLiked: Boolean = false
)
