package com.campusmov.uniride.presentation.views.routingmatching.completedCarpool

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.campusmov.uniride.R
import com.campusmov.uniride.presentation.views.routingmatching.mapcontent.MapContentViewModel

@Composable
fun CompletedCarpoolView(
    navHostController: NavHostController,
    viewModel: CompletedCarpoolViewModel = hiltViewModel(),
    viewModelMapContent: MapContentViewModel = hiltViewModel(),

    ){

    val driverProfile = viewModel.driverProfile.collectAsState()
    val currentCarpool = viewModel.currentCarpool.collectAsState()
    val user = viewModel.user.collectAsState()

    viewModel.getCarpoolById(viewModelMapContent.carpoolAcceptedId.value!!)
    currentCarpool.value?.driverId?.let { viewModel.getProfileById(it) }
    viewModel.getUserLocally()

    var rating = remember { mutableIntStateOf(0) }
    var comments = remember { mutableStateOf("") }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(6.dp)
            .background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = "S/5.10",
            color = Color.White,
            fontSize = 22.sp,
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (driverProfile.value?.profilePictureUrl?.isNotBlank() == true) {
            AsyncImage(
                model = driverProfile.value?.profilePictureUrl,
                contentDescription = "Profile picture",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
            )
        } else {
            Image(
                painter = painterResource(R.drawable.user_profile_icon),
                contentDescription = "Default profile icon",
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = "¿Cómo estuvo tu viaje con ${driverProfile.value?.firstName ?: "Desconocido"}?",
            color = Color.White,
            fontSize = 18.sp,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row (
            modifier = Modifier.align(Alignment.CenterHorizontally),
            horizontalArrangement = Arrangement.Center
        ) {
            for (i in 1..5) {
                IconButton (onClick = { rating.intValue = i }) {
                    Icon(
                        imageVector = if (i <= rating.intValue) Icons.Default.StarRate else Icons.Default.StarBorder,
                        contentDescription = "Star $i",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = comments.value,
            onValueChange = { comments.value = it },
            placeholder = { Text("Comentarios adicionales") },
            modifier = Modifier
                .width(340.dp)
                .background(Color.DarkGray, RoundedCornerShape(8.dp))
                .align(Alignment.CenterHorizontally),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.DarkGray,
                unfocusedContainerColor = Color.DarkGray,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedPlaceholderColor = Color.LightGray,
                unfocusedPlaceholderColor = Color.LightGray
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button (
            onClick = { viewModel.sendValoration(driverProfile.value?.userId!!, user.value?.id!!, rating.intValue, comments.value) },
            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .width(340.dp)
                .align(Alignment.CenterHorizontally),

            ) {
            Text(
                text = "Calificar",
                color = Color.Black,
                fontSize = 18.sp,
                )
        }


    }




}