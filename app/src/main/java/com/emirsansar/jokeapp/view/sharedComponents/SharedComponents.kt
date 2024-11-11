package com.emirsansar.jokeapp.view.sharedComponents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun TopBar(navController: NavController){
    TopAppBar(
        title = { Text(text = "Home Page") },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        backgroundColor = MaterialTheme.colorScheme.primary,
        contentColor = Color.White
    )
}

fun customGradientBrush(): Brush {
    return Brush.verticalGradient(
        colors = listOf(Color.White, Color.LightGray, Color.Gray)
    )
}

@Composable
fun AppBackground() {
    Brush.verticalGradient(
        colors = listOf(
            Color.White,
            Color.LightGray,
            Color.Gray,
        )
    )
}

@Composable
fun CenteredText(text: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = text, fontSize = 20.sp)
    }
}