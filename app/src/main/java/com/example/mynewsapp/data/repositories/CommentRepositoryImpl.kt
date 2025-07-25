package com.example.mynewsapp.data.repositories

import FirestoreUtil
import com.example.mynewsapp.data.mappers.toData
import com.example.mynewsapp.data.mappers.toDomain
import com.example.mynewsapp.data.model.comment.Comment
import com.example.mynewsapp.domain.domainmodels.CommentModel
import com.example.mynewsapp.domain.interfaces.CommentRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CommentRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore,
) : CommentRepository {

    override suspend fun addComment(commentModel: CommentModel): Result<Unit> {
        val comment = commentModel.toData()
        val user = firebaseAuth.currentUser ?: return Result.failure(Exception("User not logged in"))
        val encodedUrl = FirestoreUtil.encodeUrlToId(commentModel.url)


        val commentData = hashMapOf(
            "comment" to comment.comment,
            "profileImg" to comment.profileImg,
            "username" to comment.username,
            "userId" to user.uid,
            "commentedAt" to comment.commentedAt,
            "parentCommentId" to comment.parentCommentId,
            "isReply" to comment.isReply,
            "url" to comment.url,
            "parentUsername" to comment.parentUsername,
            "likesCount" to comment.likesCount,
        )
        return try {
            firebaseFirestore.collection("comments")
                .document(encodedUrl)
                .collection("all")
                .document(commentModel.commentedAt)
                .set(commentData).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getComments(newsUrl: String): Flow<Result<List<CommentModel>>> = callbackFlow {
        try {
            val encodedUrl = FirestoreUtil.encodeUrlToId(newsUrl)

            val listener = firebaseFirestore.collection("comments")
                .document(encodedUrl)
                .collection("all")
                .orderBy("commentedAt")
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        trySend(Result.failure(error))
                        return@addSnapshotListener
                    }
                    val result = snapshot?.documents?.mapNotNull { doc ->
                        try {
                            val comment = Comment(
                                comment = doc.getString("comment") ?: "",
                                profileImg = doc.getString("profileImg") ?: "",
                                username = doc.getString("username") ?: "",
                                userId = doc.getString("userId") ?: "",
                                commentedAt = doc.getString("commentedAt") ?: "",
                                url = doc.getString("url") ?: "",
                                isReply = doc.getBoolean("isReply") ?: false,
                                parentCommentId = doc.getString("parentCommentId"),
                                parentUsername = doc.getString("parentUsername"),
                                likesCount = doc.getLong("likesCount")?.toInt() ?: 0,
                            )

                            comment.toDomain()
                        } catch (e: Exception) {
                            null
                        }
                    } ?: emptyList()

                    trySend(Result.success(result))
                }

            awaitClose { listener.remove() }
        } catch (e: Exception) {
            trySend(Result.failure(e))
            close(e)
        }
    }
    override suspend fun likeComment(commentId: String, newsUrl: String): Result<Unit> {
        val user = firebaseAuth.currentUser ?: return Result.failure(Exception("User not logged in"))
        val encodedUrl = FirestoreUtil.encodeUrlToId(newsUrl)

        return try {
            firebaseFirestore.runTransaction { transaction ->
                val commentRef = firebaseFirestore.collection("comments")
                    .document(encodedUrl)
                    .collection("all")
                    .document(commentId)
                val likeRef = firebaseFirestore.collection("users")
                    .document(user.uid)
                    .collection("likedComments")
                    .document(commentId)

                val commentSnapshot = transaction.get(commentRef)
                val currentLikesCount = commentSnapshot.getLong("likesCount")?.toInt() ?: 0

                transaction.update(commentRef, "likesCount", currentLikesCount + 1)
                transaction.set(likeRef, hashMapOf("commentId" to commentId, "likedAt" to FieldValue.serverTimestamp()))
            }.await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun unlikeComment(commentId: String, newsUrl: String): Result<Unit> {
        val user = firebaseAuth.currentUser ?: return Result.failure(Exception("User not logged in"))
        val encodedUrl = FirestoreUtil.encodeUrlToId(newsUrl)

        return try {
            firebaseFirestore.runTransaction { transaction ->
                val commentRef = firebaseFirestore.collection("comments")
                    .document(encodedUrl)
                    .collection("all")
                    .document(commentId)
                val likeRef = firebaseFirestore.collection("users")
                    .document(user.uid)
                    .collection("likedComments")
                    .document(commentId)
                val commentSnapshot = transaction.get(commentRef)
                val currentLikesCount = commentSnapshot.getLong("likesCount")?.toInt() ?: 0

                if (currentLikesCount > 0) {
                    transaction.update(commentRef, "likesCount", currentLikesCount - 1)
                }
                transaction.delete(likeRef)
            }.await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun isCommentLiked(commentId: String): Result<Boolean> {
        val user = firebaseAuth.currentUser ?: return Result.failure(Exception("User not logged in"))
        return try {
            val documentSnapshot = firebaseFirestore.collection("users")
                .document(user.uid)
                .collection("likedComments")
                .document(commentId)
                .get()
                .await()
            Result.success(documentSnapshot.exists())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}