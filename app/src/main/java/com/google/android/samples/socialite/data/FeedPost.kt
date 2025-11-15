package com.google.android.samples.socialite.data

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class FeedPost(
    @DocumentId val id: String = "",
    val userId: String = "",
    val userName: String = "",
    val userProfilePictureUrl: String? = null,
    val text: String? = null,
    val imageUrl: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    @ServerTimestamp val timestamp: Date? = null
)
