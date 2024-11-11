package com.emirsansar.jokeapp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.emirsansar.jokeapp.viewmodel.JokeViewModel
import com.emirsansar.jokeapp.ui.theme.JokeAppTheme
import com.emirsansar.jokeapp.view.FavouritesScreen
import com.emirsansar.jokeapp.view.HomeScreen
import com.emirsansar.jokeapp.view.JokesScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JokeAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Gray
                ) {
                    MainScreen(context = applicationContext)
                }
            }
        }
    }
}


@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    context: Context
) {
    val navController = rememberNavController()
    val jokeViewModel = JokeViewModel(context)

    Scaffold(
        content = { paddingValues ->
            NavHost(navController = navController, startDestination = "home_screen") {

                composable("home_screen") {
                    HomeScreen(
                        modifier = Modifier.padding(paddingValues),
                        navController = navController,
                    )
                }

                composable("jokes_screen") {
                    JokesScreen(
                        modifier = Modifier.padding(paddingValues),
                        navController = navController,
                        jokeViewModel = jokeViewModel
                    )
                }

                composable("favourites_screen") {
                    FavouritesScreen(
                        modifier = Modifier.padding(paddingValues),
                        navController = navController,
                        jokeViewModel = jokeViewModel
                    )
                }

            }
        },
    )

}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    JokeAppTheme {

    }
}