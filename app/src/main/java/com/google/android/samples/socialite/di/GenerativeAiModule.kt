package com.google.android.samples.socialite.di

import com.google.firebase.Firebase
import com.google.firebase.ai.genai.generativeModel
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object GenerativeAiModule {

    @Provides
    fun provideGenerativeModel() = Firebase.ai.generativeModel(
        modelName = "gemini-1.5-flash",
    )

    @Provides
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }
}
