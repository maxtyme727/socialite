package com.google.android.samples.socialite.ui.reviews

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ReviewsScreen(modifier: Modifier = Modifier, locationId: String) {
    Column(modifier = modifier.fillMaxSize()) {
        Text(text = "Reviews for Location: $locationId")
        // TODO: Implement UI to display and submit reviews
    }
}
