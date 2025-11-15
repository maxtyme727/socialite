package com.google.android.samples.socialite.data.repository

import com.google.android.samples.socialite.data.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    private val usersCollection = firestore.collection("users")

    fun getUser(userId: String): Flow<User?> = flow {
        val snapshot = usersCollection.document(userId).get().await()
        emit(snapshot.toObject(User::class.java))
    }

    suspend fun createUser(user: User) {
        usersCollection.document(user.id).set(user).await()
    }

    suspend fun updateUser(user: User) {
        usersCollection.document(user.id).set(user).await()
    }
}
