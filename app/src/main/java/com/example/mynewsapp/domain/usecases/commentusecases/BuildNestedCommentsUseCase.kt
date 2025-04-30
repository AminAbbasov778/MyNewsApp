package com.example.mynewsapp.domain.usecases.commentusecases

import android.util.Log
import com.example.mynewsapp.domain.domainmodels.CommentDomainModel
import javax.inject.Inject

class BuildNestedCommentsUseCase  @Inject constructor() {

    operator fun invoke(comments: List<CommentDomainModel>): List<CommentDomainModel> {
        val topLevelComments = mutableListOf<CommentDomainModel>()
        val commentMap = mutableMapOf<String, CommentDomainModel>()
        comments.forEach { comment ->

            if (comment.isReply && comment.parentCommentId != null) {
                val parent = commentMap[comment.parentCommentId]
                if (parent != null) {
                    val updatedParent = parent.copy(
                        replies = parent.replies + comment
                    )
                    commentMap[comment.parentCommentId] = updatedParent
                }
            } else {
                topLevelComments.add(comment)
                commentMap[comment.commentedAt] = comment
            }
        }

        val result = topLevelComments.map { commentMap[it.commentedAt] ?: it }
        return result
    }
}

