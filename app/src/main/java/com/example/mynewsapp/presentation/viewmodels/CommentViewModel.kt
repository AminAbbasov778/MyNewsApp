package com.example.mynewsapp.presentation.viewmodels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynewsapp.R
import com.example.mynewsapp.data.model.userprofile.Profile
import com.example.mynewsapp.domain.domainmodels.CommentModel
import com.example.mynewsapp.domain.usecases.commentusecases.AddCommentUseCase
import com.example.mynewsapp.domain.usecases.commentusecases.GetCommentsUseCase
import com.example.mynewsapp.domain.usecases.commentusecases.IsCommentLikedUseCase
import com.example.mynewsapp.domain.usecases.commentusecases.LikeCommentUseCase
import com.example.mynewsapp.domain.usecases.commentusecases.UnlikeCommentUseCase
import com.example.mynewsapp.domain.usecases.commonusecases.GetProfileDataUseCase
import com.example.mynewsapp.domain.usecases.commonusecases.GetUserIdUseCase
import com.example.mynewsapp.domain.usecases.createnewsusecases.GetTimeStampUseCase
import com.example.mynewsapp.presentation.mappers.toDomain
import com.example.mynewsapp.presentation.mappers.toUi
import com.example.mynewsapp.presentation.uimodels.comment.CommentLikeStatus
import com.example.mynewsapp.presentation.uimodels.comment.CommentsUiModel
import com.example.mynewsapp.presentation.uimodels.comment.NewCommentUIModel
import com.example.mynewsapp.presentation.uimodels.profile.NewProfileUiModel
import com.example.mynewsapp.presentation.uimodels.profile.ProfileUiModel
import com.example.mynewsapp.presentation.uistates.UiState
import com.example.mynewsapp.presentation.uiutils.ImageUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CommentViewModel @Inject constructor(
    private val addCommentUseCase: AddCommentUseCase,
    private val getProfileDataUseCase: GetProfileDataUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,
    private val getTimeStampUseCase: GetTimeStampUseCase,
    private val getCommentsUseCase: GetCommentsUseCase,
    private val isCommentLikedUseCase: IsCommentLikedUseCase,
    private val likeCommentUseCase: LikeCommentUseCase,
    private val unLikeCommentUseCase: UnlikeCommentUseCase,
) : ViewModel() {

    private val _commentState = MutableLiveData<UiState<Int>>()
    val commentState: LiveData<UiState<Int>> get() = _commentState

    private val _comments = MutableLiveData<UiState<List<CommentsUiModel>>>()
    val comments: LiveData<UiState<List<CommentsUiModel>>> get() = _comments

    private val _selectedReplyComment = MutableLiveData<CommentsUiModel?>(null)
    val selectedReplyComment: LiveData<CommentsUiModel?> get() = _selectedReplyComment

    private val _likeState = MutableLiveData<UiState<Unit>>()
    val likeState: LiveData<UiState<Unit>> get() = _likeState

    private val _commentLikeStatus = MutableLiveData<Map<String, CommentLikeStatus>>(emptyMap())

    @RequiresApi(Build.VERSION_CODES.O)
    fun addComment(commentText: String, newsUrl: String) {
        if (commentText.isNotEmpty()) {
            _commentState.value = UiState.Loading
            viewModelScope.launch(Dispatchers.Main) {
                val profileResult = withContext(Dispatchers.IO) {
                    getUserProfile()
                }

                if (profileResult.isFailure) {
                    _commentState.value = UiState.Error(R.string.failed_add_comment)
                    return@launch
                }
                val comment = NewCommentUIModel(
                    comment = commentText,
                    profileImgBase64 = profileResult.getOrNull()!!.imageBase64,
                    username = profileResult.getOrNull()!!.username,
                    userId = getUserIdUseCase() ?: "unknown",
                    url = newsUrl,
                    commentedAt = getTimeStampUseCase(),
                    isReply = selectedReplyComment.value?.isReply ?: false,
                    parentCommentId = selectedReplyComment.value?.parentCommentId ?: "",
                    parentUsername = selectedReplyComment.value?.username ?: "",
                    likesCount = 0,
                )



                val result = withContext(Dispatchers.IO) {
                    addCommentUseCase(comment.toDomain())
                }

                if (result.isSuccess) {
                    _commentState.value = UiState.Success(R.string.success_add_comment)
                    clearData()
                } else _commentState.value = UiState.Error(R.string.failed_add_comment)

            }
        }
    }

    private suspend fun getUserProfile(): Result<NewProfileUiModel> {
        val result = getProfileDataUseCase().firstOrNull()
        return result?.let {profile ->
            if (profile.isSuccess && profile.getOrNull() != null) Result.success(profile.getOrNull()!!.toUi())
            else Result.failure(profile.exceptionOrNull() ?: Exception("Profile is not found"))

        } ?: run {
            Result.failure(Exception("Profile is not found"))
        }
    }



    @RequiresApi(Build.VERSION_CODES.O)
    fun getComments(url: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val resultFlow = getCommentsUseCase(url)
            resultFlow.collect { result ->
                withContext(Dispatchers.Main) {
                    if (result.isSuccess) {
                        val commentDomainList = result.getOrNull() ?: emptyList()
                        val commentUiList = commentDomainList.map { comment ->
                            val isLiked =
                                isCommentLikedUseCase(comment.commentedAt).getOrNull() ?: false
                            CommentsUiModel(comment.comment,
                                profileImg = ImageUtils.base64ToBitmap(comment.profileImg),
                                username = comment.username,
                                userId = comment.userId,
                                commentedAt = comment.commentedAt,
                                timeDifference = comment.timeDifference,
                                url = comment.url,
                                isReply = comment.isReply,
                                parentCommentId = comment.parentCommentId,
                                parentUsername = comment.parentUsername,
                                likesCount = comment.likesCount,
                                isLiked = isLiked,
                                replies = replyUiModel(comment.replies)
                            )
                        }
                        _comments.value = UiState.Success(commentUiList)
                        initializeLikeStatus(commentUiList)
                    } else {
                        _comments.value = UiState.Error(R.string.failed_to_get_comments)
                    }
                }
            }
        }
    }

    private suspend fun replyUiModel(replies: List<CommentModel>): List<CommentsUiModel> =
        replies.map { reply ->
            val isLiked = isCommentLikedUseCase(reply.commentedAt).getOrNull() ?: false
            CommentsUiModel(
                comment = reply.comment,
                profileImg = ImageUtils.base64ToBitmap(reply.profileImg),
                username = reply.username,
                userId = reply.userId,
                commentedAt = reply.commentedAt,
                timeDifference = reply.timeDifference,
                url = reply.url,
                isReply = reply.isReply,
                parentCommentId = reply.parentCommentId,
                parentUsername = reply.parentUsername,
                likesCount = reply.likesCount,
                isLiked = isLiked,
                replies = emptyList()
            )
        }

    fun setReplyComment(comment: CommentsUiModel) {
        _selectedReplyComment.value = if (comment.parentUsername.isNullOrEmpty()) {
            comment.copy(isReply = true, parentCommentId = comment.commentedAt)
        } else {
            comment.copy(
                isReply = true,
                parentCommentId = comment.parentCommentId,
                parentUsername = comment.username
            )
        }
    }

    fun clearData() {
        _selectedReplyComment.value = null
    }

    fun toggleLikeStatus(commentId: String, newsUrl: String, isCurrentlyLiked: Boolean) {
        if (isCurrentlyLiked) {
            unlikeComment(commentId, newsUrl)
        } else {
            likeComment(commentId, newsUrl)
        }
    }

    private fun likeComment(commentId: String, newsUrl: String) {
        _likeState.value = UiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val result = likeCommentUseCase(commentId, newsUrl)
            withContext(Dispatchers.Main) {
                if (result.isSuccess) {
                    updateCommentLikeStatus(commentId, isLiked = true)
                    _likeState.value = UiState.Success(Unit)
                } else {
                    _likeState.value = UiState.Error(R.string.failed_favorite)
                }
            }
        }
    }

    private fun unlikeComment(commentId: String, newsUrl: String) {
        _likeState.value = UiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val result = unLikeCommentUseCase(commentId, newsUrl)
            withContext(Dispatchers.Main) {
                if (result.isSuccess) {
                    updateCommentLikeStatus(commentId, isLiked = false)
                    _likeState.value = UiState.Success(Unit)
                } else {
                    _likeState.value = UiState.Error(R.string.failed_unfavorite)
                }
            }
        }
    }

    private fun initializeLikeStatus(comments: List<CommentsUiModel>) {
        val likeStatusMap = mutableMapOf<String, CommentLikeStatus>()
        comments.forEach { comment ->
            val likeStatus = CommentLikeStatus(
                commentId = comment.commentedAt,
                isLiked = comment.isLiked,
                likesCount = comment.likesCount
            )
            likeStatusMap[comment.commentedAt] = likeStatus
            comment.replies.forEach { reply ->
                val replyLikeStatus = CommentLikeStatus(
                    commentId = reply.commentedAt,
                    isLiked = reply.isLiked,
                    likesCount = reply.likesCount
                )
                likeStatusMap[reply.commentedAt] = replyLikeStatus
            }
        }
        _commentLikeStatus.value = likeStatusMap
        updateCommentsWithLikeStatus()
    }

    private fun updateCommentLikeStatus(commentId: String, isLiked: Boolean) {
        val currentStatus = _commentLikeStatus.value?.toMutableMap() ?: mutableMapOf()
        val currentLikeStatus = currentStatus[commentId] ?: CommentLikeStatus(
            commentId = commentId,
            isLiked = false,
            likesCount = 0
        )
        currentStatus[commentId] = CommentLikeStatus(
            commentId = commentId,
            isLiked = isLiked,
            likesCount = currentLikeStatus.likesCount
        )
        _commentLikeStatus.value = currentStatus
        updateCommentsWithLikeStatus()
    }

    private fun updateCommentsWithLikeStatus() {
        val currentComments = (_comments.value as? UiState.Success)?.data ?: return
        val updatedComments = currentComments.map { comment ->
            val status = _commentLikeStatus.value?.get(comment.commentedAt)
            val updatedComment = if (status != null) {
                comment.copy(
                    isLiked = status.isLiked,
                    likesCount = status.likesCount
                )
            } else comment

            val updatedReplies = updatedComment.replies.map { reply ->
                val replyStatus = _commentLikeStatus.value?.get(reply.commentedAt)
                if (replyStatus != null) {
                    reply.copy(
                        isLiked = replyStatus.isLiked,
                        likesCount = replyStatus.likesCount
                    )
                } else reply
            }
            updatedComment.copy(replies = updatedReplies)
        }
        _comments.value = UiState.Success(updatedComments)
    }
}