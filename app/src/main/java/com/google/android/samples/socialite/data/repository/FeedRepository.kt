package com.google.android.samples.socialite.data.repository

import com.google.android.samples.socialite.data.FeedPost
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FeedRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    private val feedCollection = firestore.collection("feed")

    fun getFeedPosts(
        latitude: Double,
        longitude: Double,
        radiusKm: Double = 5.0
    ): Flow<List<FeedPost>> = flow {
        // This is a simplified example. For a real app, you'd use GeoFirestore or similar
        // for efficient geospatial queries.
        val query = feedCollection
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(50) // Limit to 50 most recent posts

        val snapshot = query.get().await()
        val allPosts = snapshot.toObjects(FeedPost::class.java)

        // Basic filtering by radius (client-side for simplicity, GeoFirestore for production)
        val filteredPosts = allPosts.filter { post ->
            post.latitude != null && post.longitude != null &&
                    calculateDistance(latitude, longitude, post.latitude, post.longitude) <= radiusKm
        }
        emit(filteredPosts)
    }

    suspend fun createFeedPost(post: FeedPost) {
        feedCollection.add(post).await()
    }

    private fun calculateDistance(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double
    ): Double {
        val R = 6371.0 // Radius of Earth in kilometers

        val latDistance = Math.toRadians(lat2 - lat1)
        val lonDistance = Math.toRadians(lon2 - lon1)
        val a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        return R * c // distance in km
    }
}
