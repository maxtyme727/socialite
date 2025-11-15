package com.google.android.samples.socialite.data

import com.google.firebase.firestore.DocumentId

data class User(
    @DocumentId val id: String = "",
    val name: String = "",
    val email: String = "",
    val profilePictureUrl: String? = null,
    val bio: String? = null
)
