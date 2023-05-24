package com.anaandreis.trilhaopenai.data.models

data class CompletionRequest(
    val model: String,
    val prompt: String,
    val max_tokens: Int,
    val temperature: Float = 0f,
)