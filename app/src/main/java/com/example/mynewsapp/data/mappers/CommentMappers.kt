package com.example.mynewsapp.data.mappers

import com.example.mynewsapp.data.model.comment.Comment
import com.example.mynewsapp.domain.domainmodels.CommentModel

fun Comment.toDomain(): CommentModel{
    return   CommentModel( comment = comment,
        profileImg = profileImg,
        username = username,
        userId = userId,
        commentedAt = commentedAt,
        timeDifference = "",
        url = url,
        isReply = isReply,
        parentCommentId = parentCommentId,
        parentUsername = parentUsername,
        likesCount = likesCount,)
}

fun CommentModel.toData(): Comment{
    return Comment( comment = comment,
        profileImg = profileImg,
        username = username,
        userId = userId,
        commentedAt = commentedAt,
        url = url,
        isReply = isReply,
        parentCommentId = parentCommentId,
        parentUsername = parentUsername,
        likesCount = likesCount ,
        )
}