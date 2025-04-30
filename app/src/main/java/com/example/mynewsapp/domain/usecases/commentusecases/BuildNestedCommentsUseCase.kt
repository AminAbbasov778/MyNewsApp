package com.example.mynewsapp.domain.usecases.commentusecases

import com.example.mynewsapp.domain.domainmodels.CommentModel
import javax.inject.Inject

class BuildNestedCommentsUseCase  @Inject constructor() {

    operator fun invoke(comments: List<CommentModel>): List<CommentModel> {
        val topLevelComments = mutableListOf<CommentModel>()
        val commentMap = mutableMapOf<String, CommentModel>()
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

