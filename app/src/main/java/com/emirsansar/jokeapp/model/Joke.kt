package com.emirsansar.jokeapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// Represents a single joke with its details.
@Entity(tableName = "joke_table")
data class Joke(
    @PrimaryKey val id: Int,
    val error: Boolean,
    val joke: String?,
    val setup: String?,
    val delivery: String?,
    val safe: Boolean,
    val category: String,
    val type: String,
    val lang: String
)

// Represents a list of jokes, typically returned when requesting multiple jokes.
data class Jokes(
    val error: Boolean,
    val amount: Int,
    val jokes: List<Joke>
)