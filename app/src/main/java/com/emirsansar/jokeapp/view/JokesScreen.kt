package com.emirsansar.jokeapp.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.emirsansar.jokeapp.R
import com.emirsansar.jokeapp.model.Joke
import com.emirsansar.jokeapp.view.sharedComponents.TopBar
import com.emirsansar.jokeapp.view.sharedComponents.customGradientBrush
import com.emirsansar.jokeapp.viewmodel.JokeViewModel

@Composable
fun JokesScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    jokeViewModel: JokeViewModel
) {
    val selectedNumber = remember { mutableStateOf(1) }
    var hasUserRequestedJokes by remember { mutableStateOf(false) }

    val jokeList by jokeViewModel.jokeList.observeAsState(emptyList())
    val singleJoke by jokeViewModel.joke.observeAsState()
    val favouriteJokeList by jokeViewModel.favouriteJokeList.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        jokeViewModel.getAllFavouriteJokesFromDb()
    }

    Scaffold(
        topBar = {
            TopBar(navController)
        },
        content = { paddingValues ->
            if (!hasUserRequestedJokes) {
                InitialContent(
                    selectedNumber = selectedNumber.value,
                    onValueChange = { selectedNumber.value = it },
                    onGetClick = {
                        if (selectedNumber.value == 1) {
                            jokeViewModel.getJokeFromApi()
                        } else {
                            jokeViewModel.getJokesFromApi(selectedNumber.value)
                        }
                        hasUserRequestedJokes = true
                    },
                    paddingValues = paddingValues
                )
            }
            else {
                JokesDisplayContent(
                    selectedNumber = selectedNumber.value,
                    jokeList = jokeList,
                    singleJoke = singleJoke,
                    favouriteJokeList = favouriteJokeList,
                    onValueChange = { selectedNumber.value = it },
                    onGetClick = {
                        if (selectedNumber.value == 1) {
                            jokeViewModel.getJokeFromApi()
                        } else {
                            jokeViewModel.getJokesFromApi(selectedNumber.value)
                        }
                    },
                    paddingValues = paddingValues,
                    jokeViewModel = jokeViewModel
                )
            }
        }
    )
}

@Composable
fun InitialContent(
    selectedNumber: Int,
    onValueChange: (Int) -> Unit,
    onGetClick: () -> Unit,
    paddingValues: PaddingValues
) {
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .background(customGradientBrush()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Select the number of jokes to brighten your day!",
                fontSize = 22.sp, textAlign = TextAlign.Center,
                modifier = Modifier.width(250.dp))

            NumberSelector(
                selectedNumber = selectedNumber,
                onValueChange = onValueChange
            )

            Button(
                onClick = onGetClick,
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    containerColor = MaterialTheme.colorScheme.primary,
                ),
                modifier = Modifier.width(200.dp)
            ) {
                Text(text = "GET JOKES", color = Color.White, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}



@Composable
private fun JokesDisplayContent(
    selectedNumber: Int,
    jokeList: List<Joke>,
    singleJoke: Joke?,
    favouriteJokeList: List<Joke>,
    onValueChange: (Int) -> Unit,
    onGetClick: () -> Unit,
    jokeViewModel: JokeViewModel,
    paddingValues: PaddingValues
) {
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .background(customGradientBrush()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        JokeRequestRow(
            selectedNumber = selectedNumber,
            onValueChange = onValueChange,
            onGetClick = onGetClick
        )

        JokeList(
            jokeList = jokeList,
            singleJoke = singleJoke,
            favouriteJokeList = favouriteJokeList,
            jokeViewModel = jokeViewModel
        )
    }
}

@Composable
private fun JokeList(
    jokeList: List<Joke>? = null,
    singleJoke: Joke? = null,
    favouriteJokeList: List<Joke>,
    jokeViewModel: JokeViewModel
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(start = 10.dp, end = 10.dp, bottom = 20.dp)
    ) {
        jokeList?.let {
            items(jokeList) { joke ->
                JokeItem(
                    joke = joke,
                    favouriteJokeList = favouriteJokeList,
                    onFavourite = { jokeViewModel.insertFavouriteJokeToDb(joke) },
                    onRemove = {jokeViewModel.removeFavouriteJokeFromDb(joke) }
                )
            }
        }

        singleJoke?.let {
            item {
                JokeItem(
                    joke = it,
                    favouriteJokeList = favouriteJokeList,
                    onFavourite = { jokeViewModel.insertFavouriteJokeToDb(it) },
                    onRemove = { jokeViewModel.removeFavouriteJokeFromDb(it) }
                )
            }
        }
    }
}

@Composable
private fun JokeItem(
    joke: Joke,
    favouriteJokeList: List<Joke>,
    onFavourite: () -> Unit,
    onRemove: () -> Unit
) {
    var isFavourite by remember { mutableStateOf(favouriteJokeList.contains(joke)) }

    Box(modifier = Modifier
        .padding(top = 20.dp, start = 16.dp, end = 16.dp)
        .shadow(elevation = 4.dp, shape = RoundedCornerShape(12.dp))
        .clip(RoundedCornerShape(12.dp))
        .background(Color.Cyan)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    if (isFavourite) {
                        onRemove()
                    } else {
                        onFavourite()
                    }
                    isFavourite = !isFavourite
                },
                modifier = Modifier
                    .weight(0.2f)
                    .padding(horizontal = 10.dp)
            ) {
                val icon = if (isFavourite) {
                    painterResource(id = R.drawable.icon_star_filled)
                } else {
                    painterResource(id = R.drawable.icon_star_empty)
                }

                Icon(
                    painter = icon,
                    contentDescription = "Favourite",
                    tint = Color.Yellow,
                    modifier = Modifier.size(40.dp)
                )
            }

            Divider(
                color = Color.Black,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(2.dp)
            )

            Column(
                modifier = Modifier
                    .weight(0.8f)
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
                            .fillMaxWidth()
                            .background(Color.LightGray)
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }
            }
        }
    }
}


@Composable
private fun JokeRequestRow(
    selectedNumber: Int,
    onValueChange: (Int) -> Unit,
    onGetClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "How many jokes?", fontSize = 18.sp)

        NumberSelector(
            selectedNumber = selectedNumber,
            onValueChange = onValueChange
        )

        Button(
            onClick = onGetClick,
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.White,
                containerColor = MaterialTheme.colorScheme.primary,
            )
        ) {
            Text(text = "GET", color = Color.White)
        }
    }
}


@Composable
private fun NumberSelector(
    selectedNumber: Int,
    onValueChange: (Int) -> Unit,
    min: Int = 1,
    max: Int = 10
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Decrease IconButton
        IconButton(
            onClick = {
                if (selectedNumber > min) {
                    onValueChange(selectedNumber - 1)
                }
            },
            enabled = selectedNumber > min
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Decrease",
                tint = if (selectedNumber > min) Color.Black else Color.Gray
            )
        }

        // Display the selected number
        Text(text = selectedNumber.toString(), fontSize = 20.sp)

        // Increase IconButton
        IconButton(
            onClick = {
                if (selectedNumber < max) {
                    onValueChange(selectedNumber + 1)
                }
            },
            enabled = selectedNumber < max
        ) {
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Increase",
                tint = if (selectedNumber < max) Color.Black else Color.Gray
            )
        }
    }
}
