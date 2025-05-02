package com.example.mynewsapp.presentation.mappers

import com.example.mynewsapp.domain.domainmodels.CommentModel
import com.example.mynewsapp.presentation.uimodels.comment.CommentsUiModel
import com.example.mynewsapp.presentation.uimodels.comment.NewCommentUIModel
import com.example.mynewsapp.presentation.uiutils.ImageUtils

fun NewCommentUIModel.toDomain(): CommentModel{
    return   CommentModel( comment = comment,
        profileImg = profileImgBase64,
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

fun CommentModel.toUi(): CommentsUiModel{
    return CommentsUiModel(comment, ImageUtils.base64ToBitmap(profileImg),username,userId,commentedAt,timeDifference,url,isReply,parentCommentId,parentUsername,likesCount,isLiked,replies.map { it.toUi() })
}