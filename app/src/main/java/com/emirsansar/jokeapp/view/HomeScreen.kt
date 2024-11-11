package com.emirsansar.jokeapp.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.emirsansar.jokeapp.R
import com.emirsansar.jokeapp.view.sharedComponents.customGradientBrush

@Composable
fun HomeScreen(
    modifier: Modifier,
    navController: NavController
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(customGradientBrush()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Image(
            painter = painterResource(id = R.drawable.smiley_face),
            contentDescription = "Smiley Face",
            modifier = Modifier
                .width(150.dp)
                .clip(RoundedCornerShape(16.dp))
                .padding(top = 80.dp),
            colorFilter = ColorFilter.tint(colorScheme.primary) // hex kodu 080341
        )

        Text(
            text = "Programmer Jokes",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(top = 20.dp)
        )

        Button(
            onClick = { navController.navigate("jokes_screen") },
            colors = ButtonDefaults.buttonColors(
                containerColor = colorScheme.primary,
                contentColor = Color.White,
            ),
            modifier = Modifier
                .width(200.dp)
                .padding(top = 50.dp, bottom = 10.dp)
        ) {
            Text(text = "Get Jokes")
        }

        Button(
            onClick = { navController.navigate("favourites_screen") },
            colors = ButtonDefaults.buttonColors(
                containerColor = colorScheme.primary,
                contentColor = Color.White,
            ),
            modifier = Modifier
                .width(200.dp)
        ) {
            Text(text = "My Favourites")
        }
    }
}