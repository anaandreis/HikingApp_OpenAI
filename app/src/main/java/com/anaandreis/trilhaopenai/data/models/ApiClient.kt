package com.anaandreis.trilhaopenai.data.models

import com.anaandreis.trilhaopenai.data.OpenAiApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//responsible for creating and providing an instance of the OpenAiApi interface, which is used for making API requests to the OpenAI service.

object ApiClient {

    private const val BASE_URL = "https://api.openai.com/"


    private val httpClient = OkHttpClient.Builder()
        .build()


    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    val apiService : OpenAiApi = retrofit.create(OpenAiApi::class.java)

}