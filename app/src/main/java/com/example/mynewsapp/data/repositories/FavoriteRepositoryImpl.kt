package com.example.mynewsapp.data.repository

import FirestoreUtil
import com.example.mynewsapp.domain.interfaces.FavoriteRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : FavoriteRepository {

    override suspend fun favoriteNews(url: String): Result<Unit> {
        val user = firebaseAuth.currentUser ?: return Result.failure(Exception("User not logged in"))
        val documentId = FirestoreUtil.encodeUrlToId(url)

        return try {
            firestore.runTransaction { transaction ->
                val favoriteRef = firestore
                    .collection("users")
                    .document(user.uid)
                    .collection("favorites")
                    .document(documentId)
                val countRef = firestore
                    .collection("news")
                    .document(documentId)
                    .collection("favoritesCounts")
                    .document("count")

                val snapshot = transaction.get(countRef)
                val currentCount = snapshot.getLong("count")?.toInt() ?: 0
                val newCount = currentCount + 1

                val data = hashMapOf(
                    "url" to url,
                    "likedAt" to FieldValue.serverTimestamp()
                )
                transaction.set(favoriteRef, data)
                transaction.set(countRef, hashMapOf("count" to newCount))
            }.await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun unFavoriteNews(url: String): Result<Unit> {
        val user = firebaseAuth.currentUser ?: return Result.failure(Exception("User not logged in"))
        val documentId = FirestoreUtil.encodeUrlToId(url)

        return try {
            firestore.runTransaction { transaction ->
                val favoriteRef = firestore
                    .collection("users")
                    .document(user.uid)
                    .collection("favorites")
                    .document(documentId)
                val countRef = firestore
                    .collection("news")
                    .document(documentId)
                    .collection("favoritesCounts")
                    .document("count")

                val snapshot = transaction.get(countRef)
                val currentCount = snapshot.getLong("count")?.toInt() ?: 0
                val newCount = maxOf(0, currentCount - 1)

                transaction.delete(favoriteRef)
                transaction.set(countRef, hashMapOf("count" to newCount))
            }.await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun isNewsFavorite(url: String): Flow<Result<Boolean>> = callbackFlow {
        val user = firebaseAuth.currentUser ?: run {
            trySend(Result.failure(Exception("User not logged in")))
            close()
            return@callbackFlow
        }
        val documentId = FirestoreUtil.encodeUrlToId(url)

        try {
            val listener = firestore
                .collection("users")
                .document(user.uid)
                .collection("favorites")
                .document(documentId)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        trySend(Result.failure(error))
                        return@addSnapshotListener
                    }
                    try {
                        val isFavorite = snapshot?.exists() ?: false
                        trySend(Result.success(isFavorite))
                    } catch (e: Exception) {
                        trySend(Result.failure(e))
                    }
                }
            awaitClose { listener.remove() }
        } catch (e: Exception) {
            trySend(Result.failure(e))
            close(e)
        }
    }

    override suspend fun getFavoriteCount(url: String): Flow<Result<Int>> = callbackFlow {
        try {
            val documentId = FirestoreUtil.encodeUrlToId(url)
            val listener = firestore
                .collection("news")
                .document(documentId)
                .collection("favoritesCounts")
                .document("count")
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        trySend(Result.failure(error))
                        return@addSnapshotListener
                    }
                    try {
                        val count = snapshot?.getLong("count")?.toInt() ?: 0
                        trySend(Result.success(count))
                    } catch (e: Exception) {
                        trySend(Result.failure(e))
                    }
                }
            awaitClose { listener.remove() }
        } catch (e: Exception) {
            trySend(Result.failure(e))
            close(e)
        }
    }
}