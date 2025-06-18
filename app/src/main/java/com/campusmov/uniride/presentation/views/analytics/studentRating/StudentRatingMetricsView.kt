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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.campusmov.uniride.domain.profile.model.Profile
import com.campusmov.uniride.domain.reputation.model.Valoration

@Composable
fun StudentRatingMetricsView(
    viewModel: StudentRatingMetricsViewModel = hiltViewModel(),
    navHostController: NavHostController
) {
    val rating = viewModel.rating.collectAsState()
    val valorationList = viewModel.valorationList.collectAsState()

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
            Spacer(modifier = Modifier.height(30.dp))

            LazyColumn {
                items(valorationList.value) { valoration ->
                    ValorationComment(valoration = valoration, viewModel = viewModel)
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }


        }
    }
}

@Composable
fun ValorationComment(
    valoration: Valoration,
    viewModel: StudentRatingMetricsViewModel = hiltViewModel()
) {
    var profile = remember { mutableStateOf<Profile?>(null) }

    LaunchedEffect(valoration.senderId) {
        profile.value = viewModel.getProfileById(valoration.senderId)
    }

    Card (
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "User icon",
                tint = Color.LightGray,
                modifier = Modifier.size(40.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rating star",
                        tint = Color.Yellow,
                        modifier = Modifier.size(16.dp)
                    )

                    Text(
                        text = " ${valoration.reputationScore}  ${profile.value?.firstName.orEmpty()} ${profile.value?.lastName.orEmpty()}",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Text(
                    text = valoration.formatDate(valoration.timestamp),
                    color = Color.Gray,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )

                Text(
                    text = valoration.message ?: "No hay mensaje",
                    color = Color.White,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
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