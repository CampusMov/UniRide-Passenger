package com.campusmov.uniride.presentation.views.analytics.studentRating

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController

@Composable
fun StudentRatingMetricsView(
    viewModel: StudentRatingMetricsViewModel = hiltViewModel(),
    navHostController: NavHostController
) {
    val rating = viewModel.rating.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Black,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .background(Color.Black)
        ) {
            IconButton(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .background(Color.Transparent),
                onClick = {
                    navHostController.popBackStack()
                },
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Icon of go back", tint = Color.White)
            }

            Text(
                text = "Valoración",
                modifier = Modifier.fillMaxWidth().padding(vertical = 15.dp, horizontal = 16.dp),
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Start
                )
            Spacer(modifier = Modifier.height(30.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .background(Color.DarkGray, shape = RoundedCornerShape(16.dp))
                    .padding(12.dp)
            ) {
                Column {
                    Text(
                        text = "Valoración Promedio",
                        modifier = Modifier.fillMaxWidth().padding(vertical = 15.dp, horizontal = 16.dp),
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center
                    )

                    RatingStars(rating = rating.value)

                    Text(
                        text = "${rating.value}",
                        modifier = Modifier.fillMaxWidth().padding(vertical = 15.dp, horizontal = 16.dp),
                        color = Color.White,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center
                    )
                }
            }
            //TODO: mensajes de valoracion



        }
    }
}

@Composable
fun RatingStars(
    rating: Double,
    starCount: Int = 5
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 15.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        for (i in 1..starCount) {
            when {
                rating >= i -> {
                    Icon(
                        imageVector = Icons.Default.StarRate,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.padding(horizontal = 2.dp).size(40.dp)
                    )
                }
                rating >= i - 0.5 -> {
                    Icon(
                        imageVector = Icons.Default.StarHalf,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.padding(horizontal = 2.dp).size(40.dp)
                    )
                }
                else -> {
                    Icon(
                        imageVector = Icons.Default.StarBorder,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.padding(horizontal = 2.dp).size(40.dp)
                    )
                }
            }
        }
    }
}