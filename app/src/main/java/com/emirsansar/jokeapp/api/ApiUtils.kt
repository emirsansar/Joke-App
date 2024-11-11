package com.emirsansar.jokeapp.api

class ApiUtils {

    companion object{
        private const val BASE_URL = "https://v2.jokeapi.dev/"

        fun getJokeApiService(): ApiService {
            return try {
                ApiClient.getClient(BASE_URL)
                    .create(ApiService::class.java)
            } catch (e: Exception) {
                throw RuntimeException("An error occurred while creating the API client: ${e.message}")
            }
        }
    }

}