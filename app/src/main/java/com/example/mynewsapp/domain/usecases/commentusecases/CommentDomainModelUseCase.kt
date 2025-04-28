package com.example.mynewsapp.domain.usecases.commentusecases

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.mynewsapp.data.model.comment.CommentModel
import com.example.mynewsapp.domain.domainmodels.CommentDomainModel
import com.example.mynewsapp.domain.usecases.commonusecases.AddTimeDifferenceToNewsUseCase
import com.example.mynewsapp.domain.usecases.commonusecases.ChangeIsoToMillisFromApiUseCase
import com.example.mynewsapp.domain.usecases.commonusecases.TimeDifferenceUseCase
import com.example.mynewsapp.domain.usecases.profileUseCase.ChangeIsoToMillisFromFirebaseUseCase
import javax.inject.Inject

class CommentDomainModelUseCase @Inject constructor(val changeIsoToMillisFromFirebaseUseCase: ChangeIsoToMillisFromFirebaseUseCase, val timeDifferenceUseCase: TimeDifferenceUseCase){
    @RequiresApi(Build.VERSION_CODES.O)
    operator fun invoke(comments: List<CommentModel>): List<CommentDomainModel> =
        comments.map {
            CommentDomainModel(
                comment = it.comment,
                profileImg = it.profileImg,
                username = it.username,
                userId = it.userId,
                commentedAt = it.commentedAt,
                timeDifference = timeDifferenceUseCase(
                    changeIsoToMillisFromFirebaseUseCase(it.commentedAt)
                ),
                url = it.url,
                isReply = !it.parentCommentId.isNullOrEmpty(),
                parentCommentId = if (it.parentCommentId.isNullOrEmpty()) null else it.parentCommentId,
                parentUsername = it.parentUsername,
                replies = emptyList(),
                isLiked = it.isLiked,
                likesCount = it.likesCount
            )
        }

}