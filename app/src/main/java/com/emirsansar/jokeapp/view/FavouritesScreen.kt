package com.emirsansar.jokeapp.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Divider
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.emirsansar.jokeapp.model.Joke
import com.emirsansar.jokeapp.viewmodel.JokeViewModel
import androidx.compose.material.Scaffold
import androidx.compose.material.TextButton
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.emirsansar.jokeapp.view.sharedComponents.CenteredText
import com.emirsansar.jokeapp.view.sharedComponents.TopBar
import com.emirsansar.jokeapp.view.sharedComponents.customGradientBrush

@Composable
fun FavouritesScreen(
    modifier: Modifier,
    navController: NavController,
    jokeViewModel: JokeViewModel
) {
    val favouriteJokeList by jokeViewModel.favouriteJokeList.observeAsState(emptyList())
    var selectedJoke: Joke? = null
    var showRemoveDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        jokeViewModel.getAllFavouriteJokesFromDb()
    }

    Scaffold(
        topBar = {
            TopBar(navController)
        },
        content = { paddingValues ->
            Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(customGradientBrush()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (favouriteJokeList.isEmpty()) {
                    CenteredText(text = "No favourite jokes available.")
                }
                else {
                    FavouriteJokeList(
                        jokesList = favouriteJokeList,
                        onClick = {
                            selectedJoke = it
                            showRemoveDialog = true
                        }
                    )
                }
            }

            if (showRemoveDialog) {
                RemoveJokeDialog(setShowRemoveDialog = { showRemoveDialog = it }) {
                    if (selectedJoke != null) {
                        jokeViewModel.removeFavouriteJokeFromDb(selectedJoke!!)
                        selectedJoke = null
                    }
                }
            }
        }
    )
}


@Composable
private fun FavouriteJokeList(
    jokesList: List<Joke>,
    onClick: (Joke) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(start = 10.dp, end = 10.dp, bottom = 20.dp)
    ) {
        items(jokesList) { joke ->
            FavouriteJokeItem(joke) {
                onClick(joke)
            }
        }
    }
}

@Composable
private fun FavouriteJokeItem(
    joke: Joke,
    onClick: () -> Unit
) {
    Box(modifier = Modifier
        .padding(top = 20.dp, start = 16.dp, end = 16.dp)
        .shadow(elevation = 4.dp, shape = RoundedCornerShape(12.dp))
        .clip(RoundedCornerShape(12.dp))
        .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .background(if (joke.setup != null) Color.DarkGray else Color.LightGray),
            verticalArrangement = Arrangement.Center
        ) {
            if (!joke.setup.isNullOrEmpty()) {
                Text(
                    text = joke.setup ?: "",
                    fontSize = 19.sp,
                    modifier = Modifier
                        .background(Color.Gray)
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                )

                Divider(thickness = 2.dp, color = Color.Black)

                Text(
                    text = joke.delivery ?: "",
                    fontSize = 18.sp,
                    modifier = Modifier
                        .background(Color.LightGray)
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                )
            }
            else {
                Text(
                    text = joke.joke ?: "No joke available",
                    fontSize = 18.sp,
                    modifier = Modifier
                        .padding()
                        .background(Color.LightGray)
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                )
            }
        }
    }
}

@Composable
private fun RemoveJokeDialog(setShowRemoveDialog: (Boolean) -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        title = { Text("Remove Joke") },
        text = { Text("Are you sure you want to remove this joke?") },
        onDismissRequest = { setShowRemoveDialog(false) },
        confirmButton = {
            OutlinedButton(
                onClick = {
                    onConfirm()
                    setShowRemoveDialog(false)
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = { setShowRemoveDialog(false) }
            ) {
                Text("No")
            }
        }
    )
}