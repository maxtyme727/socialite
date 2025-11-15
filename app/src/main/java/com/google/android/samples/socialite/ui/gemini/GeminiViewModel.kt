/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law_ or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.samples.socialite.ui.gemini

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.vertexai.FirebaseVertexAI
import com.google.firebase.vertexai.type.GenerateContentResponse
import com.google.firebase.vertexai.type.generationConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GeminiViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<GeminiUiState>(GeminiUiState.Initial)
    val uiState = _uiState.asStateFlow()

    fun generateImage(prompt: String) {
        viewModelScope.launch {
            _uiState.value = GeminiUiState.Loading
            try {
                val config = generationConfig {
                    temperature = 0.7f
                    topK = 20
                    topP = 0.95f
                }
                val generativeModel = FirebaseVertexAI.getInstance()
                    .generativeModel(
                        modelName = "gemini-pro-vision",
                        generationConfig = config
                    )
                val response = generativeModel.generateContent(prompt)
                _uiState.value = GeminiUiState.Success(response)
            } catch (e: Exception) {
                _uiState.value = GeminiUiState.Error(e.message ?: "")
            }
        }
    }
}

sealed interface GeminiUiState {
    object Initial : GeminiUiState
    object Loading : GeminiUiState
    data class Success(val response: GenerateContentResponse) : GeminiUiState
    data class Error(val errorMessage: String) : GeminiUiState
}
