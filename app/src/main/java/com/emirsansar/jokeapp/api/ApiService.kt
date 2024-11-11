package com.emirsansar.jokeapp.api

import com.emirsansar.jokeapp.model.Joke
import com.emirsansar.jokeapp.model.Jokes
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("/joke/Programming")
    fun getJokes(
        @Query("blacklistFlags") blacklistFlags: String = "nsfw,religious,political,racist,sexist,explicit",
        @Query("amount") amount: Int
    ): Call<Jokes>

    @GET("/joke/Programming")
    fun getJoke(): Call<Joke>

}
