package com.emirsansar.jokeapp.repository

import com.emirsansar.jokeapp.api.ApiUtils
import com.emirsansar.jokeapp.model.Joke
import com.emirsansar.jokeapp.model.Jokes
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JokeRepository {

    private val jokeService = ApiUtils.getJokeApiService()

    // Fetches a single joke from the API.
    fun getSingleJoke(callback: (Joke) -> Unit, errorCallback: (String) -> Unit) {
        jokeService.getJoke().enqueue(object : Callback<Joke> {

            override fun onResponse(call: Call<Joke>, response: Response<Joke>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        callback(it)
                    } ?: errorCallback("No joke found.")
                } else {
                    errorCallback("Error fetching joke.")
                }
            }

            override fun onFailure(call: Call<Joke>, t: Throwable) {
                errorCallback(t.message ?: "Unknown error")
            }

        })
    }

    // Fetches multiple jokes from the API.
    fun getMultipleJokes(amount: Int, callback: (List<Joke>) -> Unit, errorCallback: (String) -> Unit) {
        jokeService.getJokes(amount = amount).enqueue(object : Callback<Jokes> {

            override fun onResponse(call: Call<Jokes>, response: Response<Jokes>) {
                if (response.isSuccessful) {
                    response.body()?.jokes?.let {
                        callback(it)
                    } ?: errorCallback("No jokes found")
                } else {
                    errorCallback("Error fetching jokes")
                }
            }

            override fun onFailure(call: Call<Jokes>, t: Throwable) {
                errorCallback(t.message ?: "Unknown error")
            }

        })
    }

}
