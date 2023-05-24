package com.anaandreis.trilhaopenai.data

import com.anaandreis.trilhaopenai.data.models.CompletionRequest
import com.anaandreis.trilhaopenai.data.models.CompletionResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

// API contract for making HTTP requests

interface OpenAiApi {
    @Headers("Authorization: Bearer ${apiKey.API_KEY}")
    @POST("v1/completions")
    suspend fun getCompletions(@Body completionResponse: CompletionRequest) : Response<CompletionResponse>
}