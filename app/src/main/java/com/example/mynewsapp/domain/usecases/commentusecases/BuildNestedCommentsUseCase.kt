package com.example.mynewsapp.domain.usecases.commentusecases

import com.example.mynewsapp.domain.domainmodels.CommentModel
import javax.inject.Inject

class BuildNestedCommentsUseCase @Inject constructor() {

    operator fun invoke(comments: List<CommentModel>): List<CommentModel> {
        val commentMap = mutableMapOf<String, CommentModel>()
        val topLevelComments = mutableListOf<CommentModel>()

        comments.forEach { comment ->
            commentMap[comment.commentedAt] = comment.copy(replies = emptyList())
            if (!comment.isReply || comment.parentCommentId == null) {
                topLevelComments.add(comment)
            }
        }
        comments.forEach { comment ->
            if (comment.isReply && comment.parentCommentId != null) {
                val parent = commentMap[comment.parentCommentId]
                if (parent != null) {
                    val updatedParent = parent.copy(
                        replies = parent.replies + comment.copy(replies = emptyList())
                    )
                    commentMap[comment.parentCommentId] = updatedParent
                }
            }
        }

        return topLevelComments.map { commentMap[it.commentedAt] ?: it }
    }
}