package com.emirsansar.jokeapp.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emirsansar.jokeapp.model.Joke
import com.emirsansar.jokeapp.dao.AppDatabase
import com.emirsansar.jokeapp.repository.JokeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class JokeViewModel(context: Context): ViewModel() {

    private val jokeRepository = JokeRepository()
    private val jokeDao = AppDatabase.getDatabase(context).jokeDao()

    // LiveData for the list of Jokes.
    private val _jokeList = MutableLiveData<List<Joke>>()
    val jokeList = _jokeList

    // LiveData for a single Joke.
    private val _joke = MutableLiveData<Joke>()
    val joke = _joke

    // LiveData for the list of Favourite Jokes.
    private val _favouriteJokeList = MutableLiveData<List<Joke>>()
    val favouriteJokeList = _favouriteJokeList


    // Fetch a list of jokes based on the amount.
    fun getJokesFromApi(amount: Int) {
        resetJokeValues()

        jokeRepository.getMultipleJokes(amount, { jokes ->
            _jokeList.postValue(jokes)
        }, { errorMessage ->
            Log.e("JokeRepository", "Error fetching jokes: $errorMessage")
            resetJokeValues()
        })
    }

    // Fetch a single joke.
    fun getJokeFromApi() {
        resetJokeValues()

        jokeRepository.getSingleJoke({ fetchedJoke ->
            _joke.postValue(fetchedJoke)  // Update the LiveData with the single joke
        }, { errorMessage ->
            Log.e("JokeRepository", "Error fetching joke: $errorMessage")
            resetJokeValues()
        })
    }

    // Resets the joke-related LiveData values to their initial state.
    private fun resetJokeValues(){
        _jokeList.postValue(emptyList())
        _joke.postValue(null)
    }

    // Inserts a joke into the database.
    fun insertFavouriteJokeToDb(joke: Joke): MutableState<Result<Unit>?> {
        val result = mutableStateOf<Result<Unit>?>(null)

        viewModelScope.launch {
            try {
                jokeDao.insert(joke)
                result.value = Result.success(Unit)
            } catch (e: Exception) {
                result.value = Result.failure(e)
                Log.e("JokeRepository", "Error inserting joke: $e")
            }
        }

        return result
    }

    // Fetch all jokes from the database.
    fun getAllFavouriteJokesFromDb() {
        viewModelScope.launch {
            try {
                val jokes = withContext(Dispatchers.IO) {
                    jokeDao.getAllJokes()
                }
                _favouriteJokeList.postValue(jokes)
            } catch (e: Exception) {
                Log.e("JokeViewModel", "Error fetching jokes: $e")
                _favouriteJokeList.postValue(emptyList())
            }
        }
    }

    // Removes a joke from the database.
    fun removeFavouriteJokeFromDb(joke: Joke): MutableState<Result<Unit>?> {
        val result = mutableStateOf<Result<Unit>?>(null)

        viewModelScope.launch {
            try {
                jokeDao.delete(joke)
                result.value = Result.success(Unit)

                if (_favouriteJokeList.value?.isEmpty() != true) {
                    val updatedList = _favouriteJokeList.value?.filter { it.id != joke.id }
                    _favouriteJokeList.postValue(updatedList)
                }
            } catch (e: Exception) {
                result.value = Result.failure(e)
            }
        }

        return result
    }

}