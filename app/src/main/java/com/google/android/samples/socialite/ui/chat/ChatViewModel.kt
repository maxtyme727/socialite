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
import com.google.android.samples.socialite.model.Chat
import com.google.android.samples.socialite.model.Message
import com.google.android.samples.socialite.repository.ChatRepository
import com.google.android.samples.socialite.repository.ChatbotRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val chatbotRepository: ChatbotRepository
) : ViewModel() {
    private var isChatbotEnabled = false

    fun getChat(chatId: String): StateFlow<Chat?> {
        return chatRepository.getChat(chatId)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = null
            )
    }

    fun sendMessage(chatId: String, text: String) {
        val message = Message(
            text = text,
            author = Firebase.auth.currentUser?.displayName ?: ""
        )
        chatRepository.sendMessage(chatId, message)
        if (isChatbotEnabled) {
            getChatbotResponse(chatId, text)
        }
    }

    fun setChatbotEnabled(isEnabled: Boolean) {
        isChatbotEnabled = isEnabled
    }

    private fun getChatbotResponse(chatId: String, message: String) {
        viewModelScope.launch {
            try {
                val response = chatbotRepository.getChatbotResponse(message)
                val chatbotMessage = Message(
                    text = response.text ?: "",
                    author = "Chatbot"
                )
                chatRepository.sendMessage(chatId, chatbotMessage)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}
