package com.example.mynewsapp.presentation.uimodels.comment

import com.example.mynewsapp.domain.domainmodels.CommentModel

class NewCommentUIModel( val comment: String,
                         val profileImg: String,
                         val username: String,
                         val userId: String,
                         val commentedAt: String,
                         val timeDifference: String?= "Empty difference",
                         val url: String,
                         val isReply: Boolean,
                         val parentCommentId: String?,
                         val parentUsername : String?,
                         val likesCount: Int = 0,
                         val isLiked: Boolean = false,
                         ) {
}