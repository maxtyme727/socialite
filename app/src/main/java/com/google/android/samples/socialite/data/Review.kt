package com.google.android.samples.socialite.data

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Review(
    @DocumentId val id: String = "",
    val userId: String = "",
    val userName: String = "",
    val userProfilePictureUrl: String? = null,
    val locationId: String = "", // ID of the hotspot/location being reviewed
    val rating: Int = 0, // e.g., 1 to 5 stars
    val text: String? = null,
    @ServerTimestamp val timestamp: Date? = null
)
