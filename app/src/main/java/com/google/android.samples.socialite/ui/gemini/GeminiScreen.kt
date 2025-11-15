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

package com.google.android.samples.socialite.ui.gemini

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.samples.socialite.R
import java.nio.ByteBuffer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeminiScreen(
    modifier: Modifier = Modifier,
    viewModel: GeminiViewModel = viewModel()
) {
    var prompt by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = prompt,
            onValueChange = { prompt = it },
            label = { Text(stringResource(R.string.prompt)) },
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { viewModel.generateImage(prompt) },
                enabled = uiState !is GeminiUiState.Loading
            ) {
                Text(stringResource(R.string.generate_image))
            }
        }

        when (uiState) {
            is GeminiUiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
            }
            is GeminiUiState.Success -> {
                val response = (uiState as GeminiUiState.Success).response
                val byteBuffer = ByteBuffer.wrap(response.candidates.first().blob.data)
                val bitmap = BitmapFactory.decodeStream(byteBuffer.array().inputStream())
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .size(256.dp)
                        .padding(top = 16.dp)
                )
                Button(
                    onClick = { /* TODO: Implement image saving */ },
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text(stringResource(R.string.save_image))
                }
            }
            is GeminiUiState.Error -> {
                Text(
                    text = (uiState as GeminiUiState.Error).errorMessage,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
            else -> {}
        }
    }
}
