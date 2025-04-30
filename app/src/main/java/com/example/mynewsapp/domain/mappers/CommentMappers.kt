package com.example.mynewsapp.domain.mappers

import com.example.mynewsapp.domain.domainmodels.CommentModel
import com.example.mynewsapp.presentation.uimodels.comment.NewCommentUIModel

fun NewCommentUIModel.toDomain(): CommentModel{
    return   CommentModel( comment = comment,
        profileImg = profileImg,
        username = username,
        userId = userId,
        commentedAt = commentedAt,
        timeDifference = timeDifference,
        url = url,
        isReply = isReply,
        parentCommentId = parentCommentId,
        parentUsername = parentUsername,
        likesCount = likesCount,)
}