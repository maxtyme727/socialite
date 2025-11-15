/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.samples.socialite.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.samples.socialite.repository.ChatbotRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatbotViewModel @Inject constructor(
    private val chatbotRepository: ChatbotRepository
) : ViewModel() {
    private val _chatbotState = MutableStateFlow<ChatbotState>(ChatbotState.Idle)
    val chatbotState = _chatbotState.asStateFlow()

    fun getChatbotResponse(message: String) {
        viewModelScope.launch {
            _chatbotState.value = ChatbotState.Loading
            try {
                val response = chatbotRepository.getChatbotResponse(message)
                _chatbotState.value = ChatbotState.Success(response.text ?: "")
            } catch (e: Exception) {
                _chatbotState.value = ChatbotState.Error(e.message ?: "")
            }
        }
    }
}

sealed interface ChatbotState {
    object Idle : ChatbotState
    object Loading : ChatbotState
    data class Success(val response: String) : ChatbotState
    data class Error(val message: String) : ChatbotState
}
