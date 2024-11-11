package com.emirsansar.jokeapp.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.emirsansar.jokeapp.model.Joke

@Dao
interface JokeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(joke: Joke)

    @Delete
    suspend fun delete(joke: Joke)

    @Query("SELECT * FROM joke_Table WHERE id = :id")
    suspend fun getJokeById(id: Int): Joke?

    @Query("SELECT * FROM joke_Table")
    fun getAllJokes(): List<Joke>

}