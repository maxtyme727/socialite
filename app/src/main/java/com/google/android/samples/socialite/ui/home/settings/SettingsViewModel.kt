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

package com.google.android.samples.socialite.ui.home.settings

import android.app.Application
import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.samples.socialite.repository.ChatRepository
import com.google.android.samples.socialite.ui.chat.ChatViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val app: Application,
    private val chatViewModel: ChatViewModel
) : ViewModel() {

    private val _isChatbotEnabled = MutableStateFlow(false)
    val isChatbotEnabled = _isChatbotEnabled.asStateFlow()

    fun clearMessageHistory() {
        // TODO: Implement
    }

    fun toggleChatbot() {
        _isChatbotEnabled.value = !_isChatbotEnabled.value
        chatViewModel.setChatbotEnabled(_isChatbotEnabled.value)
    }

    val a11yEnabled = MutableStateFlow(false)
    val isPerformanceClass = MutableStateFlow(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
}
