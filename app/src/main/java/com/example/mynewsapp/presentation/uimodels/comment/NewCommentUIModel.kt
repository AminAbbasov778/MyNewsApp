package com.example.mynewsapp.presentation.uimodels.comment

class NewCommentUIModel( val comment: String,
                         val profileImgBase64: String,
                         val username: String,
                         val userId: String,
                         val commentedAt: String,
                         val url: String,
                         val isReply: Boolean,
                         val parentCommentId: String?,
                         val parentUsername : String?,
                         val likesCount: Int = 0,
                         val isLiked: Boolean = false,
                         val timeDifference: String= "Empty difference",

                         ) {

}