package com.google.android.samples.socialite.data.repository

import com.google.android.samples.socialite.data.Review
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReviewRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    private val reviewsCollection = firestore.collection("reviews")

    fun getReviewsForLocation(locationId: String): Flow<List<Review>> = flow {
        val snapshot = reviewsCollection
            .whereEqualTo("locationId", locationId)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .await()
        emit(snapshot.toObjects(Review::class.java))
    }

    suspend fun createReview(review: Review) {
        reviewsCollection.add(review).await()
    }
}
